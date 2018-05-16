package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.Violation;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.ViolationModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.AliyunCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/16
 */
public class ViolationXKModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ViolationXKModuleStrategy.class);
    private final static long API_ID = 5L;

    public ViolationXKModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
    }

    @Override
    protected void setAPI() {
        super.api = supplyAPIRepository.findOne(API_ID);
    }

    @Override
    protected boolean isParameterComplete() {
        return super.report.getPlateNumber() != null && super.report.getCarType() != null && super.report.getEngineNo() != null && super.report.getPlateNumber() != null && super.report.getVin() != null;
    }

    @Override
    public boolean fetchData(Map<String, JSONObject> pool) {
        JSONObject cacheData = pool.get(api.getCode());
        if (cacheData != null && !EMPTY_JSON.equals(cacheData)) {
            super.apiResponse = cacheData;
            return true;
        }
        API api = new API();
        api.setSupplyAPI(super.api);
        String plateNumber = super.report.getPlateNumber();
        String vin = super.report.getVin();
        String engineNo = super.report.getEngineNo();
        String carType = super.report.getCarType();
        api.getParameters().put("plateNumber", plateNumber);
        api.getParameters().put("vin", vin);
        api.getParameters().put("engineNo", engineNo);
        api.getParameters().put("carType", carType);
        api.getHeaders().put("Content-Type", "application/json; charset=UTF-8");
        String body = "{\"plateNumber\":\"" + super.report.getPlateNumber() + "\",\"vin\":\"" + vin + "\",\"engineNo\":\"" + engineNo
                + "\",\"carType\":\"" + carType + "\"}";

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), api.getHeaders(), new HashMap<>(),
                    body, supplyAPIRepository);
            cacheData = aliyunCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null && "true".equals(cacheData.get("success").toString())) {
                putCache(api, cacheData);
            }
        }
        if (cacheData == null) {
            return false;
        } else {
            super.apiResponse = cacheData;
            return true;
        }
    }

    @Override
    public BaseModule analyseData() {
        ViolationModule violationModule = new ViolationModule(Color.SUCCESS);
        String plateNumber = super.report.getPlateNumber();
        String engineNo = super.report.getEngineNo();
        if (apiResponse != null && (boolean) apiResponse.get("success")) {
            JSONObject data = apiResponse.getJSONObject("data");
            JSONArray violations = data.getJSONArray("violations");
            if (violations != null && violations.size() > 0) {
                violationModule.setColor(Color.DANGER);
                violationModule.setCount(violations.size());
                JSONArray violations1 = new JSONArray();
                violations.forEach(row -> violations1
                        .add(new Violation(((JSONObject) row).getString("time"),
                                ((JSONObject) row).getString("address"), ((JSONObject) row).getString("reason"),
                                ((JSONObject) row).getString("fine"), ((JSONObject) row).getString("point"),
                                plateNumber, engineNo)));
                violationModule.setViolations(violations1);
            } else {
                violationModule.setColor(Color.SUCCESS);
                violationModule.setCount(0);
            }
        } else {
            violationModule.setColor(Color.TIMEOUT);
        }
        return violationModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        ViolationModule violationModule = (ViolationModule) module;
        super.report.setViolationModule(violationModule);
    }
}
