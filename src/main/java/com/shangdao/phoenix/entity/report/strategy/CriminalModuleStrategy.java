package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.Criminal;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.CriminalModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.YoufenCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/09
 */
public class CriminalModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CriminalModuleStrategy.class);

    private final static long API_ID = 8L;

    public CriminalModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
    }

    @Override
    protected void setAPI() {
        super.api = supplyAPIRepository.findOne(API_ID);
    }

    @Override
    protected boolean isParameterComplete() {
        return super.report.getCustomerName() != null && super.report.getCustomerIdCard() != null;
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
        api.getParameters().put("name", super.report.getCustomerName());
        api.getParameters().put("idcard", super.report.getCustomerIdCard());
        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            YoufenCallable youfenCallable = new YoufenCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(), new HashMap<>(), supplyAPIRepository);
            cacheData = youfenCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null && "0000".equals(cacheData.getString("resCode"))
                    && ("2012".equals(cacheData.getJSONObject("data").getString("statusCode"))
                    || "2007".equals(cacheData.getJSONObject("data").getString("statusCode")))) {
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
        CriminalModule criminalModule = new CriminalModule(Color.SUCCESS);
        if (apiResponse == null) {
            criminalModule.setColor(Color.TIMEOUT);
        } else {
            if ("0000".equals(apiResponse.get("resCode").toString())) {
                JSONObject businessData = apiResponse.getJSONObject("data");
                if ("2012".equals(businessData.getString("statusCode"))) {
                    JSONArray result = businessData.getJSONArray("result");
                    if (!result.isEmpty()) {
                        criminalModule.setColor(Color.DANGER);
                        criminalModule.setCount(result.size());
                        JSONArray criminals = new JSONArray();
                        result.forEach(row -> criminals
                                .add(new Criminal(((JSONObject) row).getString("crimeType"),
                                        ((JSONObject) row).getInteger("count"),
                                        ((JSONObject) row).getString("caseType"),
                                        ((JSONObject) row).getString("caseSource"),
                                        ((JSONObject) row).getString("casePeriod"),
                                        ((JSONObject) row).getString("caseLevel"))));
                        criminalModule.setCriminals(criminals);
                    } else {
                        criminalModule.setColor(Color.SUCCESS);
                        criminalModule.setCount(0);
                    }
                } else if ("2007".equals(businessData.getString("statusCode"))) {
                    criminalModule.setColor(Color.SUCCESS);
                    criminalModule.setCount(0);
                } else {
                    criminalModule.setColor(Color.ERROR);
                }
            } else {
                criminalModule.setColor(Color.ERROR);
            }
        }
        return criminalModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        super.report.setCriminalModule((CriminalModule) module);
    }
}
