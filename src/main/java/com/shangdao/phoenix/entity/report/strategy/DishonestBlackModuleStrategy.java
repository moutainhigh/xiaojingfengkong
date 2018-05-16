package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.DishonestBlack;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.DishonestBlackModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.AliyunCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author duyiting
 * @date 2018/04/08
 */
public class DishonestBlackModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DishonestBlackModuleStrategy.class);

    private final static long API_ID = 3L;

    public DishonestBlackModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
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
        api.getParameters().put("cardno", super.report.getCustomerIdCard());
        api.getParameters().put("type", "person");

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                    "", supplyAPIRepository);
            cacheData = aliyunCallable.call();
            logger.info(super.api.getCode() + " response:" + cacheData);
            if (cacheData != null) {
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
        DishonestBlackModule dishonestBlackModule = new DishonestBlackModule(Color.SUCCESS);
        if (apiResponse != null) {
            JSONArray data = apiResponse.getJSONArray("data");
            if (data.size() > 0) {
                dishonestBlackModule.setColor(Color.DANGER);
                dishonestBlackModule.setCount(data.size());
                JSONArray dishonestBlacks = new JSONArray();
                data.forEach(row -> dishonestBlacks
                        .add(new DishonestBlack(((HashMap) row).get("duty").toString(),
                                ((HashMap) row).get("disrupt_type").toString(), ((HashMap) row).get("code").toString(),
                                ((HashMap) row).get("pub_time").toString(), ((HashMap) row).get("court").toString(),
                                ((HashMap) row).get("area").toString(), ((HashMap) row).get("performance").toString())));
                dishonestBlackModule.setDishonestBlacks(dishonestBlacks);
            } else {
                dishonestBlackModule.setColor(Color.SUCCESS);
                dishonestBlackModule.setCount(0);
            }
        } else {
            dishonestBlackModule.setColor(Color.TIMEOUT);
        }
        return dishonestBlackModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        DishonestBlackModule dishonestBlackModule = (DishonestBlackModule) module;
        super.report.setDishonestBlackModule(dishonestBlackModule);

    }
}
