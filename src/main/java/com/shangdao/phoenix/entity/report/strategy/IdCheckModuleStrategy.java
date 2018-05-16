package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.RiskItem;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.IdCheckModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.AlipayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author duyiting
 * @date 2018/04/12
 */
public class IdCheckModuleStrategy extends SingleAPIModuleStrategy {

    private static final Logger logger = LoggerFactory.getLogger(IdCheckModuleStrategy.class);
    private static final long API_ID = 15L;

    public IdCheckModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
    }

    @Override
    protected void setAPI() {
        super.api = supplyAPIRepository.findOne(API_ID);
    }

    @Override
    protected boolean isParameterComplete() {
        return super.report.getCustomerName() != null && super.report.getCustomerIdCard() != null && super.report.getCustomerMobile() != null;
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
        api.getParameters().put("certNo", super.report.getCustomerIdCard());
        api.getParameters().put("mobile", super.report.getCustomerMobile());
        if (super.report.getCustomerBankCard() != null) {
            api.getParameters().put("bankCard", super.report.getCustomerBankCard());
        }
        if (super.report.getCustomerAddress() != null) {
            api.getParameters().put("address", super.report.getCustomerAddress());
        }

        cacheData = getCache(api);
        if (cacheData != null) {
            logger.info("缓存查询" + super.api.getCode());
        } else {
            logger.info("即时查询" + super.api.getCode());
            cacheData = AlipayUtils.antifraud(api, supplyAPIRepository);
            logger.info(super.api.getCode() + " Response:" + cacheData);
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
        IdCheckModule idCheckModule = new IdCheckModule(Color.SUCCESS);
        if (apiResponse != null) {
            List<String> verifyCode = (ArrayList<String>) (apiResponse.get("verifyCode"));
            Optional<String> cnStr = verifyCode.stream()
                    .filter(str -> verifyTable.containsKey(str) && str.startsWith("V_CN")).findFirst();
            Optional<String> phStr = verifyCode.stream()
                    .filter(str -> verifyTable.containsKey(str) && str.startsWith("V_PH")).findFirst();
            Optional<String> bcStr = verifyCode.stream()
                    .filter(str -> verifyTable.containsKey(str) && str.startsWith("V_BC")).findFirst();
            Optional<String> adStr = verifyCode.stream()
                    .filter(str -> verifyTable.containsKey(str) && str.startsWith("V_AD")).findFirst();
            if (cnStr.isPresent() && phStr.isPresent()) {
                if ("V_CN_NA".equals(cnStr.get()) || "V_CN_NM_UM".equals(cnStr.get()) || "V_PH_NA".equals(phStr.get())
                        || "V_PH_CN_UM".equals(phStr.get())) {
                    idCheckModule.setNmPhCn("姓名、手机号、身份证不一致");
                    idCheckModule.setColor(Color.DANGER);
                } else {
                    RiskItem phRisk = verifyTable.get(phStr.get());
                    String useTime = phRisk.getContent().substring(phRisk.getContent().indexOf(",") + 1);
                    idCheckModule.setNmPhCn("姓名、手机号、身份证一致," + useTime);
                    idCheckModule.setColor(phRisk.getColor());
                }
            }
            if (bcStr.isPresent()) {
                RiskItem bkRisk = verifyTable.get(bcStr.get());
                idCheckModule.setBcCn(bkRisk.getContent());
                idCheckModule.setColor(bkRisk.getColor());
            }
            if (adStr.isPresent()) {
                RiskItem adRisk = verifyTable.get(adStr.get());
                idCheckModule.setAdCn(adRisk.getContent());
                idCheckModule.setColor(adRisk.getColor());
            }
        } else {
            idCheckModule.setColor(Color.TIMEOUT);
        }
        return idCheckModule;
    }


    @Override
    public void setModuleIntoReport(BaseModule module) {
        IdCheckModule idCheckModule = (IdCheckModule) module;
        super.report.setIdCheckModule(idCheckModule);
    }

    static HashMap<String, RiskItem> verifyTable;

    static {
        verifyTable = new HashMap<>();
        verifyTable.put("V_CN_NA", new RiskItem("查询不到身份证信息", Color.WARNING));
        verifyTable.put("V_CN_NM_UM", new RiskItem("姓名与身份证不匹配", Color.DANGER));
        verifyTable.put("V_CN_NM_MA", new RiskItem("姓名与身份证匹配", Color.SUCCESS));

        verifyTable.put("V_PH_NA", new RiskItem("查询不到电话号码信息", Color.WARNING));
        verifyTable.put("V_PH_CN_UM", new RiskItem("电话号码与身份证不匹配", Color.DANGER));
        verifyTable.put("V_PH_CN_MA_UL30D", new RiskItem("电话号码与身份证匹配,1个月内有使用", Color.SUCCESS));
        verifyTable.put("V_PH_CN_MA_UL90D", new RiskItem("电话号码与身份证匹配,1个月内没有使用,3个月内有使用", Color.ATTENTION));
        verifyTable.put("V_PH_CN_MA_UL180D", new RiskItem("电话号码与身份证匹配,3个月内没有使用,6个月内有使用", Color.WARNING));
        verifyTable.put("V_PH_CN_MA_UM180D", new RiskItem("电话号码与身份证匹配,6个月内没有使用", Color.DANGER));

        verifyTable.put("V_PH_NM_UM", new RiskItem("电话号码与姓名不匹配", Color.DANGER));
        verifyTable.put("V_PH_NM_MA_UL30D", new RiskItem("电话号码与姓名匹配,1个月内有使用", Color.SUCCESS));
        verifyTable.put("V_PH_NM_MA_UL90D", new RiskItem("电话号码与姓名匹配,1个月内没有使用,3个月内有使用", Color.ATTENTION));
        verifyTable.put("V_PH_NM_MA_UL180D", new RiskItem("电话号码与姓名匹配,3个月内没有使用,6个月内有使用", Color.WARNING));
        verifyTable.put("V_PH_NM_MA_UM180D", new RiskItem("电话号码与姓名匹配,6个月内没有使用", Color.DANGER));

        verifyTable.put("V_BC_CN_UK", new RiskItem("银行卡与身份证关系未知", Color.ATTENTION));
        verifyTable.put("V_BC_CN_UM", new RiskItem("银行卡与身份证不匹配", Color.DANGER));
        verifyTable.put("V_BC_CN_MA_UL180D", new RiskItem("银行卡与身份证匹配,6个月内有使用", Color.SUCCESS));
        verifyTable.put("V_BC_CN_MA_UL360D", new RiskItem("银行卡与身份证匹配,6个月内没有使用,12个月内有使用", Color.WARNING));
        verifyTable.put("V_BC_CN_MA_UM360D", new RiskItem("银行卡与身份证匹配,12个月内没有使用", Color.DANGER));

        verifyTable.put("V_BC_PH_UK", new RiskItem("银行卡与手机号关系未知", Color.ATTENTION));
        verifyTable.put("V_BC_PH_UM", new RiskItem("银行卡与手机号不匹配", Color.DANGER));
        verifyTable.put("V_BC_PH_MA_UL180D", new RiskItem("银行卡与手机号匹配,6个月内有使用", Color.SUCCESS));
        verifyTable.put("V_BC_PH_MA_UL360D", new RiskItem("银行卡与手机号匹配,6个月内没有使用,12个月内有使用", Color.WARNING));
        verifyTable.put("V_BC_PH_MA_UM360D", new RiskItem("银行卡与手机号匹配,12个月内没有使用", Color.DANGER));

        verifyTable.put("V_AD_CN_UK", new RiskItem("地址与身份证关系未知", Color.ATTENTION));
        verifyTable.put("V_AD_CN_UM", new RiskItem("地址与身份证不匹配", Color.DANGER));
        verifyTable.put("V_AD_CN_MA_UL180D", new RiskItem("地址与身份证匹配,6个月内有使用", Color.SUCCESS));
        verifyTable.put("V_AD_CN_MA_UL360D", new RiskItem("地址与身份证匹配,6个月内没有使用,12个月内有使用", Color.WARNING));
        verifyTable.put("V_AD_CN_MA_UM360D", new RiskItem("地址与身份证匹配,12个月内没有使用", Color.DANGER));

        verifyTable.put("V_AD_PH_UK", new RiskItem("地址与手机号关系未知", Color.ATTENTION));
        verifyTable.put("V_AD_PH_UM", new RiskItem("地址与手机号不匹配", Color.DANGER));
        verifyTable.put("V_AD_PH_MA_UL180D", new RiskItem("地址与手机号匹配,6个月内有使用", Color.SUCCESS));
        verifyTable.put("V_AD_PH_MA_UL360D", new RiskItem("地址与手机号匹配,6个月内没有使用,12个月内有使用", Color.WARNING));
        verifyTable.put("V_AD_PH_MA_UM360D", new RiskItem("地址与手机号匹配,12个月内没有使用", Color.DANGER));

    }
}
