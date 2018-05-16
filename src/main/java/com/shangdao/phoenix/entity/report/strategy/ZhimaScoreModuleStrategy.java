package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.ZhimaScoreModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.AlipayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author duyiting
 * @date 2018/04/10
 */
public class ZhimaScoreModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ZhimaScoreModuleStrategy.class);
    private static final long API_ID = 13L;

    public ZhimaScoreModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
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
        api.getParameters().put("idcard", super.report.getCustomerIdCard());
        api.getParameters().put("name", super.report.getCustomerName());

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            cacheData = AlipayUtils.zhimaScore(api, supplyAPIRepository);
            logger.info(super.api.getCode() + " Response:" + cacheData);
            if (cacheData != null && cacheData.get("score") != null) {
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
        ZhimaScoreModule zhimaScoreModule = new ZhimaScoreModule(Color.SUCCESS);
        if (apiResponse == null) {
            zhimaScoreModule.setColor(Color.TIMEOUT);
        } else {
            if (apiResponse.getString("score") == null) {
                zhimaScoreModule.setColor(Color.ERROR);
            } else {
                String score = apiResponse.getString("score");
                zhimaScoreModule.setScore(score);
                if ("400+".equals(score)) {
                    zhimaScoreModule.setColor(Color.DANGER);
                } else if ("500+".equals(score)) {
                    zhimaScoreModule.setColor(Color.WARNING);
                } else if ("600+".equals(score)) {
                    zhimaScoreModule.setColor(Color.ATTENTION);
                } else if ("700+".equals(score)) {
                    zhimaScoreModule.setColor(Color.SUCCESS);
                } else {
                    zhimaScoreModule.setColor(Color.ERROR);
                }
            }
        }
        return zhimaScoreModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        ZhimaScoreModule zhimaScoreModule = (ZhimaScoreModule) module;
        super.report.setZhimaScoreModule(zhimaScoreModule);
    }


}
