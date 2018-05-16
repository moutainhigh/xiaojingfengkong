package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.module.CourtRuledDetail;
import com.shangdao.phoenix.entity.report.module.CourtRuledMoudle;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPI;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.enums.Color;
import com.shangdao.phoenix.util.XinShuCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CourtRuledStrategy extends BaseStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CourtRuledStrategy.class);

    private final static long COURT_API_ID = 19L;

    private final static long PERSON_RISK_B = 17L;

    private final SupplyAPI person_risk_b_api;
    private final SupplyAPI court_api;

    private JSONObject personRiskBResponse;
    private JSONObject courtAccurateResponse;

    private static String ACCURATE = "accurate";

    public CourtRuledStrategy(Report report, SupplyAPIRepository supplyAPIRepository,
                              APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
        this.court_api = supplyAPIRepository.findOne(COURT_API_ID);
        this.person_risk_b_api = supplyAPIRepository.findOne(PERSON_RISK_B);
    }

    @Override
    public boolean isAPIActive() {
        return person_risk_b_api.getEnabled() && court_api.getEnabled();
    }

    @Override
    public boolean isContainsAPI(Map<String, JSONObject> pool) {
        return pool.containsKey(person_risk_b_api.getCode()) && pool.containsKey(court_api.getCode() + ACCURATE);
    }

    private boolean isParameterComplete() {
        return super.report.getCustomerName() != null && super.report.getCustomerIdCard() != null && super.report.getCustomerMobile() != null;
    }

    @Override
    public boolean isAPIUnfinished(Map<String, JSONObject> pool) {
        return EMPTY_JSON.equals(pool.get(person_risk_b_api.getCode())) || EMPTY_JSON.equals(pool.get(court_api.getCode() + ACCURATE));
    }

    @Override
    public void tryPutEmptyAPI(Map<String, JSONObject> pool) {
        pool.put(person_risk_b_api.getCode(), EMPTY_JSON);
        pool.put(court_api.getCode() + ACCURATE, EMPTY_JSON);
    }

    @Override
    public void removeEmptyAPI(Map<String, JSONObject> pool) {
        pool.remove(person_risk_b_api.getCode());
        pool.remove(court_api.getCode() + ACCURATE);
    }

    @Override
    public void putAPIResponseIntoPool(Map<String, JSONObject> pool) {
        if (personRiskBResponse != null && !EMPTY_JSON.equals(personRiskBResponse)) {
            pool.put(court_api.getCode(), personRiskBResponse);
        }
        if (courtAccurateResponse != null && !EMPTY_JSON.equals(courtAccurateResponse)) {
            pool.put(court_api.getCode() + ACCURATE, courtAccurateResponse);
        }
    }

    @Override
    public boolean fetchData(Map<String, JSONObject> pool) {
        JSONObject personRiskResponse = pool.get(person_risk_b_api.getCode());
        JSONObject courtAccuratrResponse = pool.get(court_api.getCode() + ACCURATE);
        boolean isPersonRisk = false;
        boolean isCourtAccurate = false;
        if (personRiskResponse != null && !EMPTY_JSON.equals(personRiskResponse)) {
            this.personRiskBResponse = personRiskResponse;
            isPersonRisk = true;
        }
        if (courtAccuratrResponse != null && !EMPTY_JSON.equals(courtAccuratrResponse)) {
            this.courtAccurateResponse = courtAccuratrResponse;
            isCourtAccurate = true;
        }
        if (isPersonRisk && isCourtAccurate) {
            return true;
        }

        if (!isPersonRisk) {
            API api = new API();
            api.setSupplyAPI(person_risk_b_api);
            api.getParameters().put("name", super.report.getCustomerName());
            api.getParameters().put("idCard", super.report.getCustomerIdCard());
            api.getParameters().put("mobile", super.report.getCustomerMobile());
            if (!StringUtils.isEmpty(report.getCustomerBankCard())) {
                api.getParameters().put("bankCardNo", super.report.getCustomerBankCard());
            }
            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + person_risk_b_api.getCode());
            } else {
                logger.info("即时查询" + person_risk_b_api.getCode());
                XinShuCallable xinShuCallable = new XinShuCallable(api.getSupplyAPI(), new HashMap<>(), api.getParameters(), "", supplyAPIRepository);
                cacheData = xinShuCallable.call();
                logger.info(this.person_risk_b_api.getCode() + " response:" + cacheData);
                if (cacheData != null
                        && ("0000".equals(cacheData.getString("rc")) || "0001".equals(cacheData.getString("rc")))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isPersonRisk = true;
                this.personRiskBResponse = cacheData;
            }
        }

        if (analyPersonRiskB(this.personRiskBResponse) && !isCourtAccurate) {
            API api = new API();
            api.setSupplyAPI(court_api);
            api.getParameters().put("q",
                    "pname:" + super.report.getCustomerName() + "@idcardNo:" + super.report.getCustomerIdCard());
            api.getParameters().put("pageno", "1");
            api.getParameters().put("range", "100");
            api.getParameters().put("datatype", "cpws,sxgg,zxgg");

            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + court_api.getCode() + ACCURATE);
            } else {
                logger.info("即使查询" + court_api.getCode() + ACCURATE);
                XinShuCallable xinShuCallable = new XinShuCallable(api.getSupplyAPI(), new HashMap<>(),
                        api.getParameters(), "", supplyAPIRepository);
                cacheData = xinShuCallable.call();
                logger.info(this.court_api.getCode() + ACCURATE + "response:" + cacheData);
                if (cacheData != null
                        && ("0000".equals(cacheData.getString("rc")) || "0001".equals(cacheData.getString("rc")))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isCourtAccurate = true;
                this.courtAccurateResponse = cacheData;
            }

        }
        return !(!isPersonRisk && !isCourtAccurate);
    }

    @Override
    public BaseModule analyseData() {
        CourtRuledMoudle courtRuledMoudle = new CourtRuledMoudle(Color.SUCCESS);
        if (analyPersonRiskB(this.personRiskBResponse)) {
            if (courtAccurateResponse != null) {
                if ("0000".equals(courtAccurateResponse.getString("rc"))) {
                    JSONObject data = courtAccurateResponse.getJSONObject("data");
                    JSONArray allList = data.getJSONArray("allList");
                    Set<CourtRuledDetail> courtRuledDetails = new HashSet<CourtRuledDetail>();
                    allList.forEach(row -> courtRuledDetails.add(new CourtRuledDetail(row, courtRuledMoudle)));
                    courtRuledMoudle.setCourtRuledDetails(courtRuledDetails);
                    courtRuledMoudle.setCount(allList.size());
                } else if ("0001".equals(courtAccurateResponse.getString("rc"))) {
                    courtRuledMoudle.setCount(0);
                } else {
                    courtRuledMoudle.setColor(Color.ERROR);
                }
            } else {
                courtRuledMoudle.setColor(Color.TIMEOUT);
            }
        }
        return courtRuledMoudle;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        CourtRuledMoudle courtRuledMoudle = (CourtRuledMoudle) module;
        super.report.setCourtRuledMoudle(courtRuledMoudle);

    }

    private boolean analyPersonRiskB(JSONObject riskResponse) {
        if (riskResponse != null && "0000".equals(riskResponse.getString("rc"))) {
            JSONObject data = riskResponse.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            for (Object object : list) {
                JSONObject riskObject = (JSONObject) object;
                if ("C02".equals(riskObject.getString("blackFactsType")) || "C03".equals(riskObject.getString("blackFactsType"))) {
                    return true;
                }
            }
        }
        return false;
    }


}
