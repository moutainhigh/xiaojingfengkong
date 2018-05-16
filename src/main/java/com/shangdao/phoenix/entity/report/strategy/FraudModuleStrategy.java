package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.BlackRiskItem;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.FraudModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.XinShuCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/03
 */
public class FraudModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(FraudModuleStrategy.class);

    private final static long API_ID = 17L;

    public FraudModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
    }

    @Override
    protected void setAPI() {
        super.api = supplyAPIRepository.findOne(API_ID);
    }

    @Override
    protected boolean isParameterComplete() {
        return super.report.getCustomerName() != null && super.report.getCustomerIdCard() != null
                && super.report.getCustomerMobile() != null;
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
        api.getParameters().put("idCard", super.report.getCustomerIdCard());
        api.getParameters().put("mobile", super.report.getCustomerMobile());
        if (!StringUtils.isEmpty(report.getCustomerBankCard())) {
            api.getParameters().put("bankCardNo", super.report.getCustomerBankCard());
        }

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            XinShuCallable xinShuCallable = new XinShuCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                    "", supplyAPIRepository);
            cacheData = xinShuCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null && "0000".equals(cacheData.getString("rc"))) {
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
        FraudModule fraudModule = new FraudModule(Color.SUCCESS);
        if (apiResponse != null) {
            if ("0000".equals(apiResponse.get("rc").toString())) {
                JSONObject data = apiResponse.getJSONObject("data");
                JSONArray list = data.getJSONArray("list");
                JSONArray blackRisks = new JSONArray();
                for (Object o : list) {
                    JSONObject blackRiskItem = (JSONObject) o;
                    if ("D".equals(blackRiskItem.get("blackRiskType"))) {
                        BlackRiskItem item = new BlackRiskItem(blackRiskItem.getString("blackRiskType"),
                                blackRiskItem.getString("blackFactsType"),
                                blackRiskItem.getString("blackFacts"),
                                blackRiskItem.getString("blackAmt"),
                                blackRiskItem.getString("blackHappenDate"),
                                blackRiskItem.getString("blackDurationTime"),
                                blackRiskItem.getString("blackPublishSource"));
                        blackRisks.add(item);
                    }
                }
                if (blackRisks.size() > 0) {
                    fraudModule.setColor(Color.DANGER);
                }
                fraudModule.setBlackRisks(blackRisks);
            } else {
                fraudModule.setColor(Color.ERROR);
            }
        } else {
            fraudModule.setColor(Color.TIMEOUT);
        }
        return fraudModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        report.setFraudModule((FraudModule) module);
    }
}
