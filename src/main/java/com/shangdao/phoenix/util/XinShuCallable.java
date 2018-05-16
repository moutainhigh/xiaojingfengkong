package com.shangdao.phoenix.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author huanghengkun
 * @date 2018/01/26
 */
public class XinShuCallable implements Callable<JSONObject> {
    private SupplyAPI supplyAPI;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> queries;
    private String body;
    private String host;
    private String path;
    private String secretId;
    private String secretKey;

    private SupplyAPIRepository supplyAPIRepository;

    public XinShuCallable(SupplyAPI supplyAPI, HashMap<String, String> headers, final HashMap<String, String> queries, String body, SupplyAPIRepository supplyAPIRepository) {
        super();
        this.supplyAPI = supplyAPI;
        this.method = supplyAPI.getMethod().toUpperCase();
        this.headers = new HashMap<>(headers);
        this.queries = new HashMap<>(queries);
        this.body = body;
        this.host = supplyAPI.getHost();
        this.path = supplyAPI.getPath();
        this.secretId = supplyAPI.getSecretId();
        this.secretKey = supplyAPI.getSecretKey();
        this.supplyAPIRepository = supplyAPIRepository;
    }

    @Override
    public JSONObject call() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        queries.put("apikey", secretKey);
        queries.put("sign", CommonUtils.MD5Encode(secretId + sdf.format(new Date()),""));
        //noinspection Duplicates
        try {
            supplyAPI.call();
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, queries, body);
            //注：如果HTTP状态码为502/503,通常为网络不稳定,请忽略再重试
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode:" + statusCode);
            if (statusCode == 502 || statusCode == 503) {
                Thread.sleep(2000);
                supplyAPI.call();
                response = HttpUtils.doPost(host, path, method, headers, queries, body);
            }
            ObjectMapper mapper = new ObjectMapper();
            supplyAPIRepository.save(supplyAPI);
            return mapper.readValue(EntityUtils.toString(response.getEntity(), Charset.forName("utf-8")), JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
