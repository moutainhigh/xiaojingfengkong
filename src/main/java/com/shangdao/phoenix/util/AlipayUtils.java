package com.shangdao.phoenix.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZhimaCreditAntifraudVerifyRequest;
import com.alipay.api.request.ZhimaCreditScoreBriefGetRequest;
import com.alipay.api.response.ZhimaCreditAntifraudVerifyResponse;
import com.alipay.api.response.ZhimaCreditScoreBriefGetResponse;
import com.shangdao.phoenix.entity.report.strategy.BaseStrategy.API;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;

import java.util.UUID;

/**
 * @author duyiting
 * @date 2018/04/10
 */
public class AlipayUtils {

    public static JSONObject zhimaScore(API api, SupplyAPIRepository supplyAPIRepository) {
        JSONObject jsonObject = new JSONObject();
        ZhimaCreditScoreBriefGetResponse response = getScore(600, api);
        R s600 = responseToR(response);
        if (R.GT.equals(s600)) {
            R s700 = responseToR(getScore(700, api));
            if (R.GT.equals(s700)) {
                jsonObject.put("score", "700+");
            } else {
                jsonObject.put("score", "600+");
            }

        } else if (R.LT.equals(s600)) {
            R s500 = responseToR(getScore(500, api));
            if (R.GT.equals(s500)) {
                jsonObject.put("score", "500+");
            } else {
                jsonObject.put("score", "400+");
            }
        } else {
            jsonObject.put("score", null);
        }
        supplyAPIRepository.save(api.getSupplyAPI());
        return jsonObject;
    }

    public static JSONObject antifraud(API api, SupplyAPIRepository supplyAPIRepository) {
        AlipayClient alipayClient = getCilent(api);
        ZhimaCreditAntifraudVerifyRequest request = new ZhimaCreditAntifraudVerifyRequest();
        long l = UUID.randomUUID().getMostSignificantBits();
        StringBuilder bizContent = new StringBuilder("{" +
                "    \"product_code\":\"w1010100000000002859\"," +
                "    \"transaction_id\":\"" + String.valueOf(l) + "\"," +
                "    \"cert_no\":\"" + api.getParameters().get("certNo") + "\"," +
                "    \"cert_type\":\"IDENTITY_CARD\"," +
                "    \"name\":\"" + api.getParameters().get("name") + "\"," +
                "    \"mobile\":\"" + api.getParameters().get("mobile") + "\"");
        if (api.getParameters().containsKey("bankCard")) {
            bizContent.append(",    \"bank_card\":\"" + api.getParameters().get("bankCard") + "\"");
        }
        if (api.getParameters().containsKey("address")) {
            bizContent.append(",    \"address\":\"" + api.getParameters().get("address") + "\"");
        }
        bizContent.append("}");
        request.setBizContent(bizContent.toString());
        /*request.setBizContent("{" +
                "    \"product_code\":\"w1010100000000002859\"," +
                "    \"transaction_id\":\"" + String.valueOf(l) + "\"," +
                "    \"cert_no\":\"" + api.getParameters().get("certNo") + "\"," +
                "    \"cert_type\":\"IDENTITY_CARD\"," +
                "    \"name\":\"" + api.getParameters().get("name") + "\"," +
                "    \"mobile\":\"" + api.getParameters().get("mobile") + "\"," +
                "    \"bank_card\":\"" + api.getParameters().get("bankCard") + "\"," +
                "    \"address\":\"" + api.getParameters().get("address") + "\"" +
                "  }");*/
        ZhimaCreditAntifraudVerifyResponse response;
        try {
            api.getSupplyAPI().call();
            response = alipayClient.execute(request);
            supplyAPIRepository.save(api.getSupplyAPI());
            if (response.isSuccess()) {
                System.out.println("antifraud调用成功");
                JSONObject result = new JSONObject();
                result.put("verifyCode", response.getVerifyCode());
                return result;
            } else {
                System.out.println("antifraud调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private enum R {
        /**
         * 大于
         */
        GT,
        /**
         * 小于
         */
        LT,
        /**
         * 无数据
         */
        Not
    }

    private static R responseToR(ZhimaCreditScoreBriefGetResponse response) {

        if ("N/A".equals(response.getIsAdmittance())) {
            return R.Not;
        } else if ("Y".equals(response.getIsAdmittance())) {
            return R.GT;
        } else {
            return R.LT;
        }
    }

    private static AlipayClient getCilent(API api) {
        String url = api.getSupplyAPI().getHost();
        String appId = api.getSupplyAPI().getAppCode();
        String appPrivateKey = api.getSupplyAPI().getSecretKey();
        String alipayPublicKey = api.getSupplyAPI().getSecretId();
        return new DefaultAlipayClient(url, appId, appPrivateKey, "json", "UTF-8", alipayPublicKey, "RSA2");
    }

    private static ZhimaCreditScoreBriefGetResponse getScore(Integer i, API api) {
        AlipayClient alipayClient = getCilent(api);
        ZhimaCreditScoreBriefGetRequest request = new ZhimaCreditScoreBriefGetRequest();
        long l = UUID.randomUUID().getMostSignificantBits();
        request.setBizContent("{" +
                "    \"transaction_id\":\"" + String.valueOf(l) + "\"," +
                "    \"product_code\":\"w1010100000000002733\"," +
                "    \"cert_type\":\"IDENTITY_CARD\"," +
                "    \"cert_no\":\"" + api.getParameters().get("idcard") + "\"," +
                "    \"name\":\"" + api.getParameters().get("name") + "\"," +
                "    \"admittance_score\":" + i +
                "  }");
        ZhimaCreditScoreBriefGetResponse response;
        try {
            api.getSupplyAPI().call();
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("zhimascore调用成功");
                return response;

            } else {
                System.out.println("zhimascore调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;

    }

}
