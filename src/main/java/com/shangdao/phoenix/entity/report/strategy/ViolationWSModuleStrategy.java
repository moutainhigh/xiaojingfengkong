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
public class ViolationWSModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ViolationXKModuleStrategy.class);
    private final static long API_ID = 6L;

    public ViolationWSModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
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
        String mobile = super.report.getCustomerMobile();
        api.getParameters().put("lsprefix", plateNumber.substring(0, 1));
        api.getParameters().put("lsnum", plateNumber.substring(1));
        api.getParameters().put("engineno", engineNo);
        api.getParameters().put("frameno", vin);
        api.getParameters().put("lstype", carType);
        api.getParameters().put("mobile", mobile);

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                    "", supplyAPIRepository);
            cacheData = aliyunCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null && "0".equals(cacheData.get("status").toString())) {
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
        if (apiResponse != null && "0".equals(apiResponse.get("status").toString())) {
            JSONObject result = apiResponse.getJSONObject("result");
            JSONArray list = result.getJSONArray("list");
            if (list != null && list.size() > 0) {
                violationModule.setColor(Color.DANGER);
                violationModule.setCount(list.size());
                JSONArray violations = new JSONArray();
                list.forEach(row -> violations
                        .add(new Violation(((JSONObject) row).getString("time"),
                                ((JSONObject) row).getString("address"), ((JSONObject) row).getString("content"),
                                ((JSONObject) row).getString("price"), ((JSONObject) row).getString("score"),
                                plateNumber, engineNo)));
                violationModule.setViolations(violations);
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
