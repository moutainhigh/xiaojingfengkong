package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.EducationModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.YoufenCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author duyiting
 * @date 2018/04/09
 */
public class EducationModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EducationModuleStrategy.class);
    private final static long API_ID = 9L;

    public EducationModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
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
            YoufenCallable youfenCallable = new YoufenCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                    new HashMap<>(), supplyAPIRepository);
            cacheData = youfenCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null && "0000".equals(cacheData.getString("resCode"))
                    && ("2012".equals((cacheData.getJSONObject("data")).getString("statusCode")) || "2007".equals((cacheData.getJSONObject("data")).getString("statusCode")))) {
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
        String name = super.report.getCustomerName();
        String idCrad = super.report.getCustomerIdCard();
        EducationModule educationModule = new EducationModule();
        if (apiResponse == null) {
            educationModule.setColor(Color.TIMEOUT);
        } else {
            if ("0000".equals(apiResponse.getString("resCode"))) {
                JSONObject data = apiResponse.getJSONObject("data");
                if ("2012".equals(data.getString("statusCode"))) {
                    educationModule.setColor(Color.SUCCESS);
                    JSONObject result = data.getJSONObject("result");
                    educationModule.setName(name);
                    educationModule.setStudyResult(result.getString("studyResult"));
                    educationModule.setGraduateTime(result.getString("graduateTime"));
                    educationModule.setCollege(result.getString("college"));
                    educationModule.setDegree(result.getString("degree"));
                    educationModule.setCollegeType(result.getString("collegeCategory"));
                } else if ("2007".equals(data.getString("statusCode"))) {
                    educationModule.setColor(Color.SUCCESS);
                } else {
                    educationModule.setColor(Color.ERROR);
                }
            } else {
                educationModule.setColor(Color.ERROR);
            }
        }
        return educationModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        EducationModule educationModule = (EducationModule) module;
        super.report.setEducationModule(educationModule);

    }
}
