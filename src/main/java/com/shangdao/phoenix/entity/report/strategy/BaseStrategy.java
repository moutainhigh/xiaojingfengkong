package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearch;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.*;

/**
 * @author huanghengkun
 * @date 2018/04/02
 */
public abstract class BaseStrategy {

    protected Report report;
    protected SupplyAPIRepository supplyAPIRepository;
    protected APISearchRepository apiSearchRepository;

    protected final static JSONObject EMPTY_JSON = new JSONObject();

    public BaseStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        this.report = report;
        this.supplyAPIRepository = supplyAPIRepository;
        this.apiSearchRepository = apiSearchRepository;
    }

    public abstract boolean isAPIActive();

    public abstract boolean isContainsAPI(Map<String, JSONObject> pool);

    public abstract boolean isAPIUnfinished(Map<String, JSONObject> pool);

    public abstract void tryPutEmptyAPI(Map<String, JSONObject> pool);

    public abstract void removeEmptyAPI(Map<String, JSONObject> pool);

    public abstract void putAPIResponseIntoPool(Map<String, JSONObject> pool);

    public abstract boolean fetchData(Map<String, JSONObject> pool);

    public abstract BaseModule analyseData();

    public abstract void setModuleIntoReport(BaseModule module);


    public static class API {
        private SupplyAPI supplyAPI;
        private HashMap<String, String> parameters = new HashMap<>();
        private HashMap<String, String> headers = new HashMap<>();

        public SupplyAPI getSupplyAPI() {
            return supplyAPI;
        }

        public void setSupplyAPI(SupplyAPI supplyAPI) {
            this.supplyAPI = supplyAPI;
        }

        public HashMap<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(HashMap<String, String> parameters) {
            this.parameters = parameters;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

    }

    protected void putCache(API api, JSONObject apiResponse) {
        APISearch apiSearchLog = new APISearch();
        apiSearchLog.setCode(api.getSupplyAPI().getCode());
        apiSearchLog.setCreatedAt(new Date());
        apiSearchLog.setParameters(parametersToString(api.getParameters()));

        apiSearchLog.setResult(apiResponse.toJSONString());
        System.out.println(apiSearchLog.toString());
        apiSearchRepository.save(apiSearchLog);

    }

    protected JSONObject getCache(API api) {
        String parameters = parametersToString(api.getParameters());
        APISearch ret = apiSearchRepository
                .findFirstByCodeAndParametersOrderByCreatedAtDesc(api.getSupplyAPI().getCode(), parameters);
        if (ret != null) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ret.getCreatedAt());
            calendar.add(Calendar.SECOND, api.getSupplyAPI().getEffectiveTime());
            Calendar calendarNow = Calendar.getInstance();
            if (calendar.compareTo(calendarNow) < 0) {
                return null;
            }

            return JSONObject.parseObject(ret.getResult());
        } else {
            return null;
        }
    }

    private String parametersToString(Map<String, String> paramMap) {
        // 用于排序的list
        List<String> keyList = new ArrayList<>();
        for (String key : paramMap.keySet()) {
            if (paramMap.get(key) != null && !paramMap.get(key).equals("")) {
                keyList.add(key);
            }
        }
        // 对list进行排序
        Collections.sort(keyList);

        List<String> parameterList = new ArrayList<>();
        // 将排序后的参数组成字符串
        for (String key : keyList) {
            parameterList.add(key + "=" + paramMap.get(key));
        }
        return StringUtils.join(parameterList, '&');
    }
}
