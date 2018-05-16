package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;

import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/16
 */
public abstract class SingleAPIModuleStrategy extends BaseStrategy {

    protected SupplyAPI api;

    protected JSONObject apiResponse;

    public SingleAPIModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
        setAPI();
    }

    protected abstract void setAPI();

    @Override
    public boolean isAPIActive() {
        return api.getEnabled() && isParameterComplete();
    }

    protected abstract boolean isParameterComplete();

    @Override
    public boolean isContainsAPI(Map<String, JSONObject> pool) {
        return pool.containsKey(api.getCode());
    }

    @Override
    public boolean isAPIUnfinished(Map<String, JSONObject> pool) {
        return EMPTY_JSON.equals(pool.get(api.getCode()));
    }

    @Override
    public void tryPutEmptyAPI(Map<String, JSONObject> pool) {
        if (!pool.containsKey(api.getCode())) {
            pool.put(api.getCode(), EMPTY_JSON);
        }
    }

    @Override
    public void removeEmptyAPI(Map<String, JSONObject> pool) {
        pool.remove(api.getCode());
    }

    @Override
    public void putAPIResponseIntoPool(Map<String, JSONObject> pool) {
        if (apiResponse != null && !EMPTY_JSON.equals(apiResponse)) {
            pool.put(api.getCode(), apiResponse);
        }
    }
}
