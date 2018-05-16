package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.BasicInfoModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.AliyunCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/04
 */
public class BasicInfoModuleStrategy extends BaseStrategy {

    private static final Logger logger = LoggerFactory.getLogger(BasicInfoModuleStrategy.class);

    private static final long IDCARD_API_ID = 1L;
    private static final long MOBILE_API_ID = 2L;
    private static final long BANK_API_ID = 4L;

    private final SupplyAPI idcard_api;
    private final SupplyAPI mobile_api;
    private final SupplyAPI bank_api;

    private JSONObject idcardAPIResponse;
    private JSONObject mobileAPIResponse;
    private JSONObject bankAPIResponse;

    public BasicInfoModuleStrategy(Report report, SupplyAPIRepository supplyAPIRepository, APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
        this.idcard_api = supplyAPIRepository.findOne(IDCARD_API_ID);
        this.mobile_api = supplyAPIRepository.findOne(MOBILE_API_ID);
        this.bank_api = supplyAPIRepository.findOne(BANK_API_ID);
    }

    @Override
    public boolean isAPIActive() {
        return idcard_api.getEnabled() && mobile_api.getEnabled() && bank_api.getEnabled();
    }

    private boolean isParameterComplete() {
        return super.report.getCustomerIdCard() != null && super.report.getCustomerMobile() != null
                && super.report.getCustomerBankCard() != null;
    }

    @Override
    public boolean isContainsAPI(Map<String, JSONObject> pool) {
        return pool.containsKey(idcard_api.getCode()) && pool.containsKey(mobile_api.getCode()) && pool.containsKey(bank_api.getCode());
    }

    @Override
    public boolean isAPIUnfinished(Map<String, JSONObject> pool) {
        return EMPTY_JSON.equals(pool.get(idcard_api.getCode())) || EMPTY_JSON.equals(pool.get(mobile_api.getCode())) || EMPTY_JSON.equals(pool.get(bank_api.getCode()));
    }

    @Override
    public void tryPutEmptyAPI(Map<String, JSONObject> pool) {
        pool.put(idcard_api.getCode(), EMPTY_JSON);
        pool.put(mobile_api.getCode(), EMPTY_JSON);
        pool.put(bank_api.getCode(), EMPTY_JSON);
    }

    @Override
    public void removeEmptyAPI(Map<String, JSONObject> pool) {
        pool.remove(idcard_api.getCode());
        pool.remove(mobile_api.getCode());
        pool.remove(bank_api.getCode());
    }

    @Override
    public void putAPIResponseIntoPool(Map<String, JSONObject> pool) {
        if (idcardAPIResponse != null && !EMPTY_JSON.equals(idcardAPIResponse)) {
            pool.put(idcard_api.getCode(), idcardAPIResponse);
        }
        if (mobileAPIResponse != null && !EMPTY_JSON.equals(mobileAPIResponse)) {
            pool.put(mobile_api.getCode(), mobileAPIResponse);
        }
        if (bankAPIResponse != null && !EMPTY_JSON.equals(bankAPIResponse)) {
            pool.put(bank_api.getCode(), bankAPIResponse);
        }
    }

    @Override
    public boolean fetchData(Map<String, JSONObject> pool) {
        JSONObject idcardAPIResponse = pool.get(idcard_api.getCode());
        JSONObject mobileAPIResponse = pool.get(mobile_api.getCode());
        JSONObject bankAPIResponse = pool.get(bank_api.getCode());
        boolean isIdcardFetched = false;
        boolean isMobileFetched = false;
        boolean isBankFetched = false;
        if (idcardAPIResponse != null && !EMPTY_JSON.equals(idcardAPIResponse)) {
            this.idcardAPIResponse = idcardAPIResponse;
            isIdcardFetched = true;
        }
        if (mobileAPIResponse != null && !EMPTY_JSON.equals(mobileAPIResponse)) {
            this.mobileAPIResponse = mobileAPIResponse;
            isMobileFetched = true;
        }
        if (bankAPIResponse != null && !EMPTY_JSON.equals(bankAPIResponse)) {
            this.bankAPIResponse = bankAPIResponse;
            isBankFetched = true;
        }
        if (isIdcardFetched && isMobileFetched && isBankFetched) {
            return true;
        }

        if (!isIdcardFetched) {
            API api = new API();
            api.setSupplyAPI(idcard_api);
            api.getParameters().put("idcard", super.report.getCustomerIdCard());

            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + idcard_api.getCode());
            } else {
                logger.info("即时查询" + idcard_api.getCode());
                AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(), "", supplyAPIRepository);
                cacheData = aliyunCallable.call();
                logger.info(this.idcard_api.getCode() + " response:" + cacheData);
                if (cacheData != null && "0".equals(cacheData.getString("status"))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isIdcardFetched = true;
                this.idcardAPIResponse = cacheData;
            }
        }
        if (!isMobileFetched) {
            API api = new API();
            api.setSupplyAPI(mobile_api);
            api.getParameters().put("mobile", super.report.getCustomerMobile());
            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + mobile_api.getCode());
            } else {
                logger.info("即时查询" + mobile_api.getCode());
                AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                        "", supplyAPIRepository);
                cacheData = aliyunCallable.call();
                logger.info(this.mobile_api.getCode() + " response:" + cacheData);
                if (cacheData != null && "0".equals(cacheData.getString("error_code"))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isMobileFetched = true;
                this.mobileAPIResponse = cacheData;
            }
        }
        if (!isBankFetched) {
            API api = new API();
            api.setSupplyAPI(bank_api);
            api.getParameters().put("bankcard", super.report.getCustomerBankCard());
            // 版本号
            api.getParameters().put("apiversion", "2.0.5");
            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + bank_api.getCode());
            } else {
                logger.info("即时查询" + bank_api.getCode());
                AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(),
                        "", supplyAPIRepository);
                cacheData = aliyunCallable.call();
                logger.info(this.bank_api.getCode() + " response:" + cacheData);
                if (cacheData != null && "0".equals(cacheData.getString("error_code"))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isBankFetched = true;
                this.bankAPIResponse = cacheData;
            }
        }
        // 当3个全部失败的时候才返回false
        return !(!isIdcardFetched && !isMobileFetched && !isBankFetched);
    }

    @Override
    public BaseModule analyseData() {
        BasicInfoModule basicInfoModule = new BasicInfoModule(Color.SUCCESS);
        basicInfoModule.setName(super.report.getCustomerName());
        basicInfoModule.setIdCard(super.report.getCustomerIdCard());
        basicInfoModule.setAddress(super.report.getCustomerAddress());
        basicInfoModule.setMobile(super.report.getCustomerMobile());
        basicInfoModule.setBankCard(super.report.getCustomerBankCard());
        if (idcardAPIResponse != null) {
            if ("0".equals(idcardAPIResponse.getString("status"))) {
                JSONObject result = idcardAPIResponse.getJSONObject("result");
                basicInfoModule.setSex(result.getString("sex"));
                basicInfoModule.setProvince(result.getString("province"));
                basicInfoModule.setCity(result.getString("city"));
                basicInfoModule.setTown(result.getString("town"));
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    Date birth = sdf.parse(result.getString("birth"));
                    int year1 = Calendar.getInstance().get(Calendar.YEAR);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(birth);
                    int year2 = calendar.get(Calendar.YEAR);
                    basicInfoModule.setAge(year1 - year2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                basicInfoModule.setColor(Color.ERROR);
            }
        } else {
            basicInfoModule.setColor(Color.TIMEOUT);
        }
        if (mobileAPIResponse != null) {
            if ("0".equals(mobileAPIResponse.getString("error_code"))) {
                JSONObject result = mobileAPIResponse.getJSONObject("result");
                String mobileCompany = result.getString("city") + result.getString("company").substring(2);
                basicInfoModule.setMobileCompany(mobileCompany);
            } else {
                basicInfoModule.setColor(Color.ERROR);
            }
        } else {
            basicInfoModule.setColor(Color.TIMEOUT);
        }
        if (bankAPIResponse != null) {
            if ("0".equals(bankAPIResponse.getString("error_code"))) {
                JSONObject result = bankAPIResponse.getJSONObject("result");
                String bankName = result.getString("bankname");
                basicInfoModule.setBankName(bankName);
            } else {
                basicInfoModule.setColor(Color.ERROR);
            }
        } else {
            basicInfoModule.setColor(Color.TIMEOUT);
        }
        return basicInfoModule;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        BasicInfoModule basicInfoModule = (BasicInfoModule) module;
        super.report.setBasicInfoModule(basicInfoModule);
    }
}
