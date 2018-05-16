package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author huanghengkun
 * @date 2018/04/03
 */
@Service
public class SupplyAPIService {
    @Autowired
    InitService initService;

    @Autowired
    SupplyAPIRepository supplyAPIRepository;

    @PostConstruct
    public void init() {
        if (supplyAPIRepository.count() == 0) {
            SupplyAPI aliyunIdcard = new SupplyAPI();
            aliyunIdcard.setId(1L);
            aliyunIdcard.setName("ali身份证分析");
            aliyunIdcard.setCode("aliyunIdcard");
            aliyunIdcard.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunIdcard.setEffectiveTime(360000000);
            aliyunIdcard.setHost("http://jisuidcard.market.alicloudapi.com");
            aliyunIdcard.setPath("/idcard/query");
            aliyunIdcard.setMethod("GET");
            aliyunIdcard.setPrice(0.001);
            aliyunIdcard.setCallCount(0L);
            aliyunIdcard.setTotalCount(0L);
            aliyunIdcard.setRemainderCount(0L);
            aliyunIdcard.setEnabled(true);
            supplyAPIRepository.save(aliyunIdcard);

            SupplyAPI aliyunMobile = new SupplyAPI();
            aliyunMobile.setId(2L);
            aliyunMobile.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunMobile.setName("ali手机号码归属地");
            aliyunMobile.setCode("aliyunMobile");
            aliyunMobile.setEffectiveTime(360000000);
            aliyunMobile.setHost("http://mobileas.market.alicloudapi.com");
            aliyunMobile.setPath("/mobile");
            aliyunMobile.setMethod("GET");
            aliyunMobile.setPrice(0.0001);
            aliyunMobile.setCallCount(0L);
            aliyunMobile.setTotalCount(0L);
            aliyunMobile.setRemainderCount(0L);
            aliyunMobile.setEnabled(true);
            supplyAPIRepository.save(aliyunMobile);

            SupplyAPI aliyunDishonestblack = new SupplyAPI();
            aliyunDishonestblack.setId(3L);
            aliyunDishonestblack.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunDishonestblack.setName("ali失信黑名单");
            aliyunDishonestblack.setCode("aliyunDishonestBlack");
            aliyunDishonestblack.setEffectiveTime(604800);
            aliyunDishonestblack.setHost("http://blacklist.market.alicloudapi.com");
            aliyunDishonestblack.setPath("/apixcredit/blacklist/dishonest");
            aliyunDishonestblack.setMethod("GET");
            aliyunDishonestblack.setPrice(0.01);
            aliyunDishonestblack.setCallCount(0L);
            aliyunDishonestblack.setTotalCount(0L);
            aliyunDishonestblack.setRemainderCount(0L);
            aliyunDishonestblack.setEnabled(true);
            supplyAPIRepository.save(aliyunDishonestblack);

            SupplyAPI aliyunBankCard = new SupplyAPI();
            aliyunBankCard.setId(4L);
            aliyunBankCard.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunBankCard.setName("ali银行卡信息查询带归属地");
            aliyunBankCard.setCode("aliyunBankcard");
            aliyunBankCard.setEffectiveTime(360000000);
            aliyunBankCard.setHost("http://api43.market.alicloudapi.com");
            aliyunBankCard.setPath("/api/c43");
            aliyunBankCard.setMethod("GET");
            aliyunBankCard.setPrice(0.0059);
            aliyunBankCard.setCallCount(0L);
            aliyunBankCard.setTotalCount(0L);
            aliyunBankCard.setRemainderCount(0L);
            aliyunBankCard.setEnabled(true);
            supplyAPIRepository.save(aliyunBankCard);

            SupplyAPI aliyunViolationXk = new SupplyAPI();
            aliyunViolationXk.setId(5L);
            aliyunViolationXk.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunViolationXk.setName("ali全国车辆违章查询(小卡科技)");
            aliyunViolationXk.setCode("aliyunViolationXK");
            aliyunViolationXk.setEffectiveTime(86400);
            aliyunViolationXk.setHost("http://ddycapi.market.alicloudapi.com");
            aliyunViolationXk.setPath("/violation/query");
            aliyunViolationXk.setMethod("POST");
            aliyunViolationXk.setPrice(0.1);
            aliyunViolationXk.setCallCount(0L);
            aliyunViolationXk.setTotalCount(0L);
            aliyunViolationXk.setRemainderCount(0L);
            aliyunViolationXk.setEnabled(true);
            supplyAPIRepository.save(aliyunViolationXk);

            SupplyAPI aliyunViolationWs = new SupplyAPI();
            aliyunViolationWs.setId(6L);
            aliyunViolationWs.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunViolationWs.setName("ali全国车辆交通违章查询(网尚科技)");
            aliyunViolationWs.setCode("aliyunViolationWS");
            aliyunViolationWs.setEffectiveTime(86400);
            aliyunViolationWs.setHost("http://jisuqgclwz.market.alicloudapi.com");
            aliyunViolationWs.setPath("/illegal/query");
            aliyunViolationWs.setMethod("GET");
            aliyunViolationWs.setPrice(0.036);
            aliyunViolationWs.setCallCount(0L);
            aliyunViolationWs.setTotalCount(0L);
            aliyunViolationWs.setRemainderCount(0L);
            aliyunViolationWs.setEnabled(true);
            supplyAPIRepository.save(aliyunViolationWs);

            SupplyAPI aliyunOcrvehicle = new SupplyAPI();
            aliyunOcrvehicle.setId(7L);
            aliyunOcrvehicle.setAppCode("41022859e7f84583b81d1fc355130af5");
            aliyunOcrvehicle.setName("ali印刷文字识别_行驶证识别");
            aliyunOcrvehicle.setCode("aliyunOcrVehicle");
            aliyunOcrvehicle.setEffectiveTime(360000000);
            aliyunOcrvehicle.setHost("http://dm-53.data.aliyun.com");
            aliyunOcrvehicle.setPath("/rest/160601/ocr/ocr_vehicle.json");
            aliyunOcrvehicle.setMethod("POST");
            aliyunOcrvehicle.setPrice(0.00002);
            aliyunOcrvehicle.setCallCount(0L);
            aliyunOcrvehicle.setTotalCount(0L);
            aliyunOcrvehicle.setRemainderCount(0L);
            aliyunOcrvehicle.setEnabled(true);
            supplyAPIRepository.save(aliyunOcrvehicle);

            SupplyAPI youfenCriminal = new SupplyAPI();
            youfenCriminal.setId(8L);
            youfenCriminal.setAppCode("shangdao041");
            youfenCriminal.setHost("https://api.acedata.com.cn:2443");
            youfenCriminal.setName("youfen刑事案底核查");
            youfenCriminal.setCode("youfenCriminal");
            youfenCriminal.setEffectiveTime(604800);
            youfenCriminal.setPath("/oreo/personal/crimeInfo");
            youfenCriminal.setMethod("GET");
            youfenCriminal.setPrice(1.3);
            youfenCriminal.setCallCount(0L);
            youfenCriminal.setTotalCount(0L);
            youfenCriminal.setRemainderCount(0L);
            youfenCriminal.setEnabled(true);
            supplyAPIRepository.save(youfenCriminal);

            SupplyAPI youfenEducation = new SupplyAPI();
            youfenEducation.setId(9L);
            youfenEducation.setAppCode("shangdao041");
            youfenEducation.setHost("https://api.acedata.com.cn:2443");
            youfenEducation.setName("youfen学历学籍信息查询");
            youfenEducation.setCode("youfenEducation");
            youfenEducation.setEffectiveTime(604800);
            youfenEducation.setPath("/oreo/personal/education/detail");
            youfenEducation.setMethod("GET");
            youfenEducation.setPrice(2.3);
            youfenEducation.setCallCount(0L);
            youfenEducation.setTotalCount(0L);
            youfenEducation.setRemainderCount(0L);
            youfenEducation.setEnabled(true);
            supplyAPIRepository.save(youfenEducation);

            SupplyAPI youfenMultipleheadlend = new SupplyAPI();
            youfenMultipleheadlend.setId(10L);
            youfenMultipleheadlend.setAppCode("shangdao041");
            youfenMultipleheadlend.setHost("https://api.acedata.com.cn:2443");
            youfenMultipleheadlend.setName("youfen多头借贷全量核查");
            youfenMultipleheadlend.setCode("youfenMultipleHeadLend");
            youfenMultipleheadlend.setEffectiveTime(604800);
            youfenMultipleheadlend.setPath("/oreo/personal/creditInfoAll");
            youfenMultipleheadlend.setMethod("GET");
            youfenMultipleheadlend.setPrice(1.4);
            youfenMultipleheadlend.setCallCount(0L);
            youfenMultipleheadlend.setTotalCount(0L);
            youfenMultipleheadlend.setRemainderCount(0L);
            youfenMultipleheadlend.setEnabled(true);
            supplyAPIRepository.save(youfenMultipleheadlend);

            SupplyAPI youfenCourtjudgment = new SupplyAPI();
            youfenCourtjudgment.setId(11L);
            youfenCourtjudgment.setAppCode("shangdao041");
            youfenCourtjudgment.setHost("https://api.acedata.com.cn:2443");
            youfenCourtjudgment.setName("youfen个人法院判决简项");
            youfenCourtjudgment.setCode("youfenCourtJudgment");
            youfenCourtjudgment.setEffectiveTime(604800);
            youfenCourtjudgment.setPath("/oreo/personal/courtJudgment/simple/query");
            youfenCourtjudgment.setMethod("GET");
            youfenCourtjudgment.setPrice(0.36);
            youfenCourtjudgment.setCallCount(0L);
            youfenCourtjudgment.setTotalCount(0L);
            youfenCourtjudgment.setRemainderCount(0L);
            youfenCourtjudgment.setEnabled(true);
            supplyAPIRepository.save(youfenCourtjudgment);

            SupplyAPI youfenCourtjudgmentdetail = new SupplyAPI();
            youfenCourtjudgmentdetail.setId(12L);
            youfenCourtjudgmentdetail.setAppCode("shangdao041");
            youfenCourtjudgmentdetail.setHost("https://api.acedata.com.cn:2443");
            youfenCourtjudgmentdetail.setName("youfen个人判决文书详情");
            youfenCourtjudgmentdetail.setCode("youfenCourtJudgmentDetail");
            youfenCourtjudgmentdetail.setEffectiveTime(360000000);
            youfenCourtjudgmentdetail.setPath("/oreo/courtJudgment/detail/query");
            youfenCourtjudgmentdetail.setMethod("GET");
            youfenCourtjudgmentdetail.setPrice(1.00);
            youfenCourtjudgmentdetail.setCallCount(0L);
            youfenCourtjudgmentdetail.setTotalCount(0L);
            youfenCourtjudgmentdetail.setRemainderCount(0L);
            youfenCourtjudgmentdetail.setEnabled(true);
            supplyAPIRepository.save(youfenCourtjudgmentdetail);

            SupplyAPI alipayZhimascore = new SupplyAPI();
            alipayZhimascore.setId(13L);
            alipayZhimascore.setHost("https://openapi.alipay.com/gateway.do");
            alipayZhimascore.setAppCode("2017080808093876");
            //原privateKey
            alipayZhimascore.setSecretKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDN/Nqyd4QORfF7CArpwBOgVaBAlmZYjOLMduhHUJKE2O+S5SzRddOShLLRfP87LGekpPuqbd9xJcZVj6wRQW5wG4Z/oW9Rno7PzWlyUG6cpX2AHASMEuF+kIV5kxqB9rHsOEIqmkctmjYIqA85U/8yu4Ar4APtEtJpUwrHad4ZNLYKAlpBmPQtbtMg+e2M/K/dLYx/jtYnJZ5hTw+23vNW+3hgO4FclJrOeFFp3R6rw+auEQcXAmPKTzh9ILI/gwFjAixZap+h+aD7kF0xvW8DNaTnCzs0sIXNG4SaO+QGyqBBdy2JasIbziKrdU6Onr+D+OUg9CxnwYFzBs8xublXAgMBAAECggEAaDFkdi2aMJYUhcUVsBMmWk21l7X74wKwnffvmlMZKfWJiWY++1x8Pnq88NYnTEkzy8ZgPl8BTPxsjE72HGxabn6d0+VWp5RqbJZcpiQlLztOdgS2EBxkz15PBaXxHJgqe3Ahl+cd5eVBO7O2r6rbRVAujwLrOXnx0LDenOviGiD1Mri2p8FW1CKiV0NT9RmLWWuBzcFfR+OlokUQA5fC5I6dEMnxwwdyDolVlXq7k2gG3+K0JXMxRT7FMyL+3HHdkr9Rj0iEslA3jBS6S04go5dsYvBpCUPkroEtd0Ebcm35iUFRaQIcy7ytOYSg6okCGoAxQAPvbb3I36Hg3UHUYQKBgQDpmxvbTVGoonfC5OM/DcdCeaQRgknsJCk+M70bFsK8FWs+nrdjob+rgkmulhmgOAYpH+a1eSYOpqiYFE6bZ6unHLm6h7g7WAUhi8svRPMt9EWemMvi/BEliWkVAuKPJF5jLeR31C0QBgy6S5cTOEHxJwLXOQHjIwncWh8mK3LQLwKBgQDhu/ip+u9wkYSU8EGnzlJSQpImqmXhjbSHjD4GiViaEgcTz/MFpMRekCj7dZ/O14yrcGf5jGHRVCXP8yDvikFxn2DEnWZPYqzlZt8vj9bPsdHMjd/dOFpSdB42AlTorkUKISh1eEyGOPVnRsF4ThWpaDvSMbu1QxIh0do0MK73WQKBgFVMV72Z2+lReZ1majvJ0ipJtEjYZBmfkpet2K0dgHmg8Inq9leg5hW/+xuOn0jLbCR0C4/T7ruGyyTMtW81SY45hO/BQtoYZ7c6DjTyXkE3YnxomnoFRFWKeR488XEG/JgGV9gRV2r0ao1TsJRKBHoDnpvrqu6/Coi1N45+auvtAoGALmf3UfKNgOXZjMjrpnTLBbA73YkQUtNP9sHFCpD3zwy/ubn5awKZgBtA4Zkn1l0HxULhjTLn9SHIeDyyW6xfkd1DBoiZPN67Tpncbimy7pXO3K+aAx+Pqf3HLDFEhWU2OkPgzm2BxeI1yRqJsZLT7zTHf4nL3hLAyoUax7nublkCgYEA1/o79qXWu4vztxnJWxW7WYp6PFAsFbcwdnhFNJ9IQ57L062iFwgXY3H4SXsghrmnoiSum1WPBObiwg0jYF2wAOreLzGoIJqpYvUSArhXxsBaSklgkNris5q144n5htOJdC7yv6p4KdbpXtbZ/Lb8QtndvWzKEZM91KAKwQmJBZg=");
            //原publicKey
            alipayZhimascore.setSecretId("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmD7svb0lTKZ66IX3+Y/wCCjVgiCpJlb+9r/ZPfs8+YUaV0Z2P4DlmdJ01MLqAIQgd0xjoUg1isjoFKqjQOQOO2g/jqJsonyapM0dT/I4eT225GPe4WsKi0vEARtJ4us+R5XVpNowLe1pBLT03cc0WschbazZGO+KlWRmYPYUijQ0l4A6t2/V5CWwkoQi04vlnHmy6t2i73V1SVSj/4tiZ5MGmb8vzOXmGW7/nH4jNGWZSYlbDu6Z0BMg+Poa6b5Qx9ASeqx1H2181GZ/v7CU4Y2bJx3+h79T4DMPnjVcl9HyUN1IiWb+bBmlOOL8bOrkaZcnnEkj0EfdrwpM4PsglwIDAQAB");
            alipayZhimascore.setName("alipay芝麻积分");
            alipayZhimascore.setCode("alipayZhimaScore");
            alipayZhimascore.setEffectiveTime(604800);
            alipayZhimascore.setMethod("POST");
            alipayZhimascore.setPrice(0.00);
            alipayZhimascore.setCallCount(0L);
            alipayZhimascore.setTotalCount(0L);
            alipayZhimascore.setRemainderCount(0L);
            alipayZhimascore.setEnabled(true);
            supplyAPIRepository.save(alipayZhimascore);

            SupplyAPI alipayWatchlist = new SupplyAPI();
            alipayWatchlist.setId(14L);
            alipayWatchlist.setHost("https://openapi.alipay.com/gateway.do");
            alipayWatchlist.setAppCode("2017080808093876");
            //原privateKey
            alipayWatchlist.setSecretKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDN/Nqyd4QORfF7CArpwBOgVaBAlmZYjOLMduhHUJKE2O+S5SzRddOShLLRfP87LGekpPuqbd9xJcZVj6wRQW5wG4Z/oW9Rno7PzWlyUG6cpX2AHASMEuF+kIV5kxqB9rHsOEIqmkctmjYIqA85U/8yu4Ar4APtEtJpUwrHad4ZNLYKAlpBmPQtbtMg+e2M/K/dLYx/jtYnJZ5hTw+23vNW+3hgO4FclJrOeFFp3R6rw+auEQcXAmPKTzh9ILI/gwFjAixZap+h+aD7kF0xvW8DNaTnCzs0sIXNG4SaO+QGyqBBdy2JasIbziKrdU6Onr+D+OUg9CxnwYFzBs8xublXAgMBAAECggEAaDFkdi2aMJYUhcUVsBMmWk21l7X74wKwnffvmlMZKfWJiWY++1x8Pnq88NYnTEkzy8ZgPl8BTPxsjE72HGxabn6d0+VWp5RqbJZcpiQlLztOdgS2EBxkz15PBaXxHJgqe3Ahl+cd5eVBO7O2r6rbRVAujwLrOXnx0LDenOviGiD1Mri2p8FW1CKiV0NT9RmLWWuBzcFfR+OlokUQA5fC5I6dEMnxwwdyDolVlXq7k2gG3+K0JXMxRT7FMyL+3HHdkr9Rj0iEslA3jBS6S04go5dsYvBpCUPkroEtd0Ebcm35iUFRaQIcy7ytOYSg6okCGoAxQAPvbb3I36Hg3UHUYQKBgQDpmxvbTVGoonfC5OM/DcdCeaQRgknsJCk+M70bFsK8FWs+nrdjob+rgkmulhmgOAYpH+a1eSYOpqiYFE6bZ6unHLm6h7g7WAUhi8svRPMt9EWemMvi/BEliWkVAuKPJF5jLeR31C0QBgy6S5cTOEHxJwLXOQHjIwncWh8mK3LQLwKBgQDhu/ip+u9wkYSU8EGnzlJSQpImqmXhjbSHjD4GiViaEgcTz/MFpMRekCj7dZ/O14yrcGf5jGHRVCXP8yDvikFxn2DEnWZPYqzlZt8vj9bPsdHMjd/dOFpSdB42AlTorkUKISh1eEyGOPVnRsF4ThWpaDvSMbu1QxIh0do0MK73WQKBgFVMV72Z2+lReZ1majvJ0ipJtEjYZBmfkpet2K0dgHmg8Inq9leg5hW/+xuOn0jLbCR0C4/T7ruGyyTMtW81SY45hO/BQtoYZ7c6DjTyXkE3YnxomnoFRFWKeR488XEG/JgGV9gRV2r0ao1TsJRKBHoDnpvrqu6/Coi1N45+auvtAoGALmf3UfKNgOXZjMjrpnTLBbA73YkQUtNP9sHFCpD3zwy/ubn5awKZgBtA4Zkn1l0HxULhjTLn9SHIeDyyW6xfkd1DBoiZPN67Tpncbimy7pXO3K+aAx+Pqf3HLDFEhWU2OkPgzm2BxeI1yRqJsZLT7zTHf4nL3hLAyoUax7nublkCgYEA1/o79qXWu4vztxnJWxW7WYp6PFAsFbcwdnhFNJ9IQ57L062iFwgXY3H4SXsghrmnoiSum1WPBObiwg0jYF2wAOreLzGoIJqpYvUSArhXxsBaSklgkNris5q144n5htOJdC7yv6p4KdbpXtbZ/Lb8QtndvWzKEZM91KAKwQmJBZg=");
            //原publicKey
            alipayWatchlist.setSecretId("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmD7svb0lTKZ66IX3+Y/wCCjVgiCpJlb+9r/ZPfs8+YUaV0Z2P4DlmdJ01MLqAIQgd0xjoUg1isjoFKqjQOQOO2g/jqJsonyapM0dT/I4eT225GPe4WsKi0vEARtJ4us+R5XVpNowLe1pBLT03cc0WschbazZGO+KlWRmYPYUijQ0l4A6t2/V5CWwkoQi04vlnHmy6t2i73V1SVSj/4tiZ5MGmb8vzOXmGW7/nH4jNGWZSYlbDu6Z0BMg+Poa6b5Qx9ASeqx1H2181GZ/v7CU4Y2bJx3+h79T4DMPnjVcl9HyUN1IiWb+bBmlOOL8bOrkaZcnnEkj0EfdrwpM4PsglwIDAQAB");
            alipayWatchlist.setName("alipay行业关注名单普惠版");
            alipayWatchlist.setCode("alipayWatchList");
            alipayWatchlist.setEffectiveTime(86400);
            alipayWatchlist.setMethod("POST");
            alipayWatchlist.setPrice(0.25);
            alipayWatchlist.setCallCount(0L);
            alipayWatchlist.setTotalCount(0L);
            alipayWatchlist.setRemainderCount(0L);
            alipayWatchlist.setEnabled(true);
            supplyAPIRepository.save(alipayWatchlist);

            SupplyAPI alipayAntifraud = new SupplyAPI();
            alipayAntifraud.setId(15L);
            alipayAntifraud.setHost("https://openapi.alipay.com/gateway.do");
            alipayAntifraud.setAppCode("2017080808093876");
            //原privateKey
            alipayAntifraud.setSecretKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDN/Nqyd4QORfF7CArpwBOgVaBAlmZYjOLMduhHUJKE2O+S5SzRddOShLLRfP87LGekpPuqbd9xJcZVj6wRQW5wG4Z/oW9Rno7PzWlyUG6cpX2AHASMEuF+kIV5kxqB9rHsOEIqmkctmjYIqA85U/8yu4Ar4APtEtJpUwrHad4ZNLYKAlpBmPQtbtMg+e2M/K/dLYx/jtYnJZ5hTw+23vNW+3hgO4FclJrOeFFp3R6rw+auEQcXAmPKTzh9ILI/gwFjAixZap+h+aD7kF0xvW8DNaTnCzs0sIXNG4SaO+QGyqBBdy2JasIbziKrdU6Onr+D+OUg9CxnwYFzBs8xublXAgMBAAECggEAaDFkdi2aMJYUhcUVsBMmWk21l7X74wKwnffvmlMZKfWJiWY++1x8Pnq88NYnTEkzy8ZgPl8BTPxsjE72HGxabn6d0+VWp5RqbJZcpiQlLztOdgS2EBxkz15PBaXxHJgqe3Ahl+cd5eVBO7O2r6rbRVAujwLrOXnx0LDenOviGiD1Mri2p8FW1CKiV0NT9RmLWWuBzcFfR+OlokUQA5fC5I6dEMnxwwdyDolVlXq7k2gG3+K0JXMxRT7FMyL+3HHdkr9Rj0iEslA3jBS6S04go5dsYvBpCUPkroEtd0Ebcm35iUFRaQIcy7ytOYSg6okCGoAxQAPvbb3I36Hg3UHUYQKBgQDpmxvbTVGoonfC5OM/DcdCeaQRgknsJCk+M70bFsK8FWs+nrdjob+rgkmulhmgOAYpH+a1eSYOpqiYFE6bZ6unHLm6h7g7WAUhi8svRPMt9EWemMvi/BEliWkVAuKPJF5jLeR31C0QBgy6S5cTOEHxJwLXOQHjIwncWh8mK3LQLwKBgQDhu/ip+u9wkYSU8EGnzlJSQpImqmXhjbSHjD4GiViaEgcTz/MFpMRekCj7dZ/O14yrcGf5jGHRVCXP8yDvikFxn2DEnWZPYqzlZt8vj9bPsdHMjd/dOFpSdB42AlTorkUKISh1eEyGOPVnRsF4ThWpaDvSMbu1QxIh0do0MK73WQKBgFVMV72Z2+lReZ1majvJ0ipJtEjYZBmfkpet2K0dgHmg8Inq9leg5hW/+xuOn0jLbCR0C4/T7ruGyyTMtW81SY45hO/BQtoYZ7c6DjTyXkE3YnxomnoFRFWKeR488XEG/JgGV9gRV2r0ao1TsJRKBHoDnpvrqu6/Coi1N45+auvtAoGALmf3UfKNgOXZjMjrpnTLBbA73YkQUtNP9sHFCpD3zwy/ubn5awKZgBtA4Zkn1l0HxULhjTLn9SHIeDyyW6xfkd1DBoiZPN67Tpncbimy7pXO3K+aAx+Pqf3HLDFEhWU2OkPgzm2BxeI1yRqJsZLT7zTHf4nL3hLAyoUax7nublkCgYEA1/o79qXWu4vztxnJWxW7WYp6PFAsFbcwdnhFNJ9IQ57L062iFwgXY3H4SXsghrmnoiSum1WPBObiwg0jYF2wAOreLzGoIJqpYvUSArhXxsBaSklgkNris5q144n5htOJdC7yv6p4KdbpXtbZ/Lb8QtndvWzKEZM91KAKwQmJBZg=");
            //原publicKey
            alipayAntifraud.setSecretId("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmD7svb0lTKZ66IX3+Y/wCCjVgiCpJlb+9r/ZPfs8+YUaV0Z2P4DlmdJ01MLqAIQgd0xjoUg1isjoFKqjQOQOO2g/jqJsonyapM0dT/I4eT225GPe4WsKi0vEARtJ4us+R5XVpNowLe1pBLT03cc0WschbazZGO+KlWRmYPYUijQ0l4A6t2/V5CWwkoQi04vlnHmy6t2i73V1SVSj/4tiZ5MGmb8vzOXmGW7/nH4jNGWZSYlbDu6Z0BMg+Poa6b5Qx9ASeqx1H2181GZ/v7CU4Y2bJx3+h79T4DMPnjVcl9HyUN1IiWb+bBmlOOL8bOrkaZcnnEkj0EfdrwpM4PsglwIDAQAB");
            alipayAntifraud.setName("alipay反欺诈");
            alipayAntifraud.setCode("alipayAntiFraud");
            alipayAntifraud.setEffectiveTime(2592000);
            alipayAntifraud.setMethod("POST");
            alipayAntifraud.setPrice(0.15);
            alipayAntifraud.setCallCount(0L);
            alipayAntifraud.setTotalCount(0L);
            alipayAntifraud.setRemainderCount(0L);
            alipayAntifraud.setEnabled(true);
            supplyAPIRepository.save(alipayAntifraud);

            SupplyAPI xinshuRiskA = new SupplyAPI();
            xinshuRiskA.setId(16L);
            xinshuRiskA.setSecretId("bcr5h");
            xinshuRiskA.setSecretKey("CD929D020BC2D7C0E76746E047C48C7A");
            xinshuRiskA.setHost("http://123.59.76.144");
            xinshuRiskA.setName("xinshu个人风险信息综合查询(A)");
            xinshuRiskA.setCode("xinshuRiskA");
            xinshuRiskA.setEffectiveTime(604800);
            xinshuRiskA.setPath("/ws/black/compQuery");
            xinshuRiskA.setMethod("POST");
            xinshuRiskA.setPrice(2.5);
            xinshuRiskA.setCallCount(0L);
            xinshuRiskA.setTotalCount(0L);
            xinshuRiskA.setRemainderCount(0L);
            xinshuRiskA.setEnabled(true);
            supplyAPIRepository.save(xinshuRiskA);

            SupplyAPI xinshuRiskB = new SupplyAPI();
            xinshuRiskB.setId(17L);
            xinshuRiskB.setSecretId("bcr5h");
            xinshuRiskB.setSecretKey("CD929D020BC2D7C0E76746E047C48C7A");
            xinshuRiskB.setHost("http://123.59.76.144");
            xinshuRiskB.setName("xinshu个人风险信息综合查询(B)");
            xinshuRiskB.setCode("xinshuRiskB");
            xinshuRiskB.setEffectiveTime(604800);
            xinshuRiskB.setPath("/ws/black/compBQuery");
            xinshuRiskB.setMethod("POST");
            xinshuRiskB.setPrice(1.2);
            xinshuRiskB.setCallCount(0L);
            xinshuRiskB.setTotalCount(0L);
            xinshuRiskB.setRemainderCount(0L);
            xinshuRiskB.setEnabled(true);
            supplyAPIRepository.save(xinshuRiskB);

            SupplyAPI xinshuAuthMobile = new SupplyAPI();
            xinshuAuthMobile.setId(18L);
            xinshuAuthMobile.setSecretId("bcr5h");
            xinshuAuthMobile.setSecretKey("CD929D020BC2D7C0E76746E047C48C7A");
            xinshuAuthMobile.setHost("http://123.59.76.144");
            xinshuAuthMobile.setName("xinshu手机号实名认证");
            xinshuAuthMobile.setCode("xinshuAuthMobile");
            xinshuAuthMobile.setEffectiveTime(604800);
            xinshuAuthMobile.setPath("/ws/person/authMobile");
            xinshuAuthMobile.setMethod("POST");
            xinshuAuthMobile.setPrice(0.6);
            xinshuAuthMobile.setCallCount(0L);
            xinshuAuthMobile.setTotalCount(0L);
            xinshuAuthMobile.setRemainderCount(0L);
            xinshuAuthMobile.setEnabled(true);
            supplyAPIRepository.save(xinshuAuthMobile);

            SupplyAPI xinshuCourt = new SupplyAPI();
            xinshuCourt.setId(19L);
            xinshuCourt.setSecretId("bcr5h");
            xinshuCourt.setSecretKey("CD929D020BC2D7C0E76746E047C48C7A");
            xinshuCourt.setHost("http://123.59.76.144");
            xinshuCourt.setName("xinshu法院数据综合查询");
            xinshuCourt.setCode("xinshuCourt");
            xinshuCourt.setEffectiveTime(604800);
            xinshuCourt.setPath("/ws/court/query");
            xinshuCourt.setMethod("POST");
            xinshuCourt.setPrice(1.2);
            xinshuCourt.setCallCount(0L);
            xinshuCourt.setTotalCount(0L);
            xinshuCourt.setRemainderCount(0L);
            xinshuCourt.setEnabled(true);
            supplyAPIRepository.save(xinshuCourt);


        }
    }
}
