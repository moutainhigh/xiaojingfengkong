package com.shangdao.phoenix.entity.report.strategy;

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
import com.shangdao.phoenix.util.AliyunCallable;
import com.shangdao.phoenix.util.XinShuCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CourtRuledSuspectedStrategy extends BaseStrategy {

    private static final Logger logger = LoggerFactory.getLogger(CourtRuledStrategy.class);

    private final static long COURT_API_ID = 19L;

    private final static long IDCARD_API_ID = 1L;

    private final SupplyAPI idcard_api;
    private final SupplyAPI court_api;

    private JSONObject idcardResponse;
    private JSONObject courtResponse;

    public CourtRuledSuspectedStrategy(Report report, SupplyAPIRepository supplyAPIRepository,
                                       APISearchRepository apiSearchRepository) {
        super(report, supplyAPIRepository, apiSearchRepository);
        this.idcard_api = supplyAPIRepository.findOne(IDCARD_API_ID);
        this.court_api = supplyAPIRepository.findOne(COURT_API_ID);
    }

    @Override
    public boolean isAPIActive() {
        return idcard_api.getEnabled() && court_api.getEnabled();
    }

    @Override
    public boolean isContainsAPI(Map<String, JSONObject> pool) {
        return pool.containsKey(idcard_api.getCode()) && pool.containsKey(court_api.getCode());
    }

    @Override
    public boolean isAPIUnfinished(Map<String, JSONObject> pool) {
        return EMPTY_JSON.equals(pool.get(idcard_api.getCode())) || EMPTY_JSON.equals(pool.get(court_api.getCode()));
    }

    private boolean isParameterComplete() {
        return super.report.getCustomerName() != null && super.report.getCustomerIdCard() != null;
    }

    @Override
    public void tryPutEmptyAPI(Map<String, JSONObject> pool) {
        pool.put(idcard_api.getCode(), EMPTY_JSON);
        pool.put(court_api.getCode(), EMPTY_JSON);
    }

    @Override
    public void removeEmptyAPI(Map<String, JSONObject> pool) {
        pool.remove(idcard_api.getCode());
        pool.remove(court_api.getCode());
    }

    @Override
    public void putAPIResponseIntoPool(Map<String, JSONObject> pool) {
        if (idcardResponse != null && !EMPTY_JSON.equals(idcardResponse)) {
            pool.put(idcard_api.getCode(), idcardResponse);
        }
        if (courtResponse != null && !EMPTY_JSON.equals(courtResponse)) {
            pool.put(court_api.getCode(), courtResponse);
        }
    }

    @Override
    public boolean fetchData(Map<String, JSONObject> pool) {
        JSONObject idcardResponse = pool.get(idcard_api.getCode());
        JSONObject courtResponse = pool.get(court_api.getCode());
        boolean isIdcardFetched = false;
        boolean isCourtFetched = false;
        if (idcardResponse != null && !EMPTY_JSON.equals(idcardResponse)) {
            this.idcardResponse = idcardResponse;
            isIdcardFetched = true;
        }
        if (courtResponse != null && !EMPTY_JSON.equals(courtResponse)) {
            this.courtResponse = courtResponse;
            isCourtFetched = true;
        }
        if (isIdcardFetched && isCourtFetched) {
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
                AliyunCallable aliyunCallable = new AliyunCallable(api.getSupplyAPI(), new HashMap<>(),
                        api.getParameters(), "", supplyAPIRepository);
                cacheData = aliyunCallable.call();
                logger.info(this.idcard_api.getCode() + " response:" + cacheData);
                if (cacheData != null && "0".equals(cacheData.getString("status"))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isIdcardFetched = true;
                this.idcardResponse = cacheData;
            }
        }

        if (!isCourtFetched) {
            API api = new API();
            api.setSupplyAPI(court_api);
            api.getParameters().put("q", super.report.getCustomerName());
            api.getParameters().put("pageno", "1");
            api.getParameters().put("range", "100");
            api.getParameters().put("datatype", "");

            JSONObject cacheData = getCache(api);
            if (cacheData != null) {
                logger.info("缓存查询" + court_api.getCode());
            } else {
                logger.info("即使查询" + court_api.getCode());
                XinShuCallable xinShuCallable = new XinShuCallable(api.getSupplyAPI(), new HashMap<>(),
                        api.getParameters(), "", supplyAPIRepository);
                cacheData = xinShuCallable.call();
                logger.info(this.court_api.getCode() + "response:" + cacheData);
                if (cacheData != null
                        && ("0000".equals(cacheData.getString("rc")) || "0001".equals(cacheData.getString("rc")))) {
                    putCache(api, cacheData);
                }
            }
            if (cacheData != null) {
                isCourtFetched = true;
                this.courtResponse = cacheData;
            }
        }
        return !(!isIdcardFetched && !isCourtFetched);
    }

    @Override
    public BaseModule analyseData() {
        CourtRuledMoudle courtRuledMoudle = new CourtRuledMoudle(Color.SUCCESS);
        if (courtResponse != null) {
            if ("0000".equals(courtResponse.getString("rc"))) {
                JSONObject data = courtResponse.getJSONObject("data");
                JSONArray allList = data.getJSONArray("allList");
                Set<CourtRuledDetail> courtRuledDetails = selectAllList(allList, courtRuledMoudle);
                courtRuledMoudle.setCourtRuledDetails(courtRuledDetails);
                courtRuledMoudle.setCount(courtRuledDetails.size());
            } else if ("0001".equals(courtResponse.getString("rc"))) {
                courtRuledMoudle.setCount(0);
            } else {
                courtRuledMoudle.setColor(Color.ERROR);
            }
        } else {
            courtRuledMoudle.setColor(Color.TIMEOUT);
        }
        return courtRuledMoudle;
    }

    @Override
    public void setModuleIntoReport(BaseModule module) {
        CourtRuledMoudle courtRuleSuspecteddMoudle = (CourtRuledMoudle) module;
        super.report.setCourtRuledSuspectedMoudle(courtRuleSuspecteddMoudle);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 后续处理疑似案件

    private Set<CourtRuledDetail> selectAllList(JSONArray suspectedArray, CourtRuledMoudle moudle) {
        Set<CourtRuledDetail> courtRuledDetails = new HashSet<CourtRuledDetail>();
        if (suspectedArray != null && suspectedArray.size() > 0) {
            for (Object object : suspectedArray) {
                JSONObject suspectedObject = (JSONObject) object;
                if (suspectedObject.containsKey("cpwsId") && analyCpws(suspectedObject)) {
                    CourtRuledDetail cpwsdDetail = new CourtRuledDetail(suspectedObject, moudle);
                    courtRuledDetails.add(cpwsdDetail);
                    continue;
                } else if (suspectedObject.containsKey("ktggId") && analyKtgg(suspectedObject)) {
                    CourtRuledDetail ktggDetail = new CourtRuledDetail(suspectedObject, moudle);
                    courtRuledDetails.add(ktggDetail);
                    continue;
                } else if (suspectedObject.containsKey("zxggId") && analyZxgg(suspectedObject)) {
                    CourtRuledDetail zxggdDetail = new CourtRuledDetail(suspectedObject, moudle);
                    courtRuledDetails.add(zxggdDetail);
                    continue;
                } else if (suspectedObject.containsKey("sxggId") && analySxgg(suspectedObject)) {
                    CourtRuledDetail sxggdDetail = new CourtRuledDetail(suspectedObject, moudle);
                    courtRuledDetails.add(sxggdDetail);
                    continue;
                } else if (suspectedObject.containsKey("fyggId") && analyFygg(suspectedObject)) {
                    CourtRuledDetail fyggdDetail = new CourtRuledDetail(suspectedObject, moudle);
                    courtRuledDetails.add(fyggdDetail);
                    continue;
                }
            }
        }
        return courtRuledDetails;
    }

    //////////////////具体处理疑似案件的方法
    private boolean analyCpws(JSONObject cpws) {
        return false;

    }

    private boolean analyKtgg(JSONObject ktgg) {
        return false;

    }

    private boolean analySxgg(JSONObject sxgg) {
        return false;

    }

    private boolean analyZxgg(JSONObject zxgg) {
        return false;

    }

    private boolean analyFygg(JSONObject fygg) {
        return false;

    }

}
