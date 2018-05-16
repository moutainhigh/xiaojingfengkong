package com.shangdao.phoenix.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author huanghengkun
 * @date 2018/01/10
 */
public class YoufenCallable implements Callable<JSONObject> {
    private SupplyAPI supplyAPI;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queries;
    private Map<String, String> bodies;
    private String host;
    private String path;
    private String appcode;

    private SupplyAPIRepository supplyAPIRepository;

    public YoufenCallable(SupplyAPI supplyAPI, HashMap<String, String> headers, final HashMap<String, String> queries, HashMap<String, String> bodies, SupplyAPIRepository supplyAPIRepository) {
        super();
        this.supplyAPI = supplyAPI;
        this.method = supplyAPI.getMethod().toUpperCase();
        this.headers = new HashMap<>(headers);
        this.queries = new HashMap<>(queries);
        this.bodies = new HashMap<>(bodies);
        this.host = supplyAPI.getHost();
        this.path = supplyAPI.getPath();
        this.appcode = supplyAPI.getAppCode();
        this.supplyAPIRepository = supplyAPIRepository;
    }

    @Override
    public JSONObject call() {
        queries.put("account", appcode);
        //noinspection Duplicates
        try{
            supplyAPI.call();
            HttpResponse response = HttpUtils.doGet(host,path,method,headers, queries);
            supplyAPIRepository.save(supplyAPI);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(EntityUtils.toString(response.getEntity(),Charset.forName("utf-8")),JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
