package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.Application;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.BO.Criminal;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.CriminalModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author huanghengkun
 * @date 2018/04/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class CriminalModuleStrategyMockTest {

    @Autowired
    private SupplyAPIRepository supplyAPIRepository;

    @Autowired
    private APISearchRepository apiSearchRepository;

    @Test
    public void test() {
        String response = "{\"data\":{\"statusCode\":\"2012\",\"statusMsg\":\"查询成功\",\"result\":[{\"crimeType\":\"比中前科\",\"count\":\"1\",\"caseType\":\"侵犯财产案\",\"caseSource\":\"前科\",\"casePeriod\":\"5-10年（不含）以内\",\"caseLevel\":\"严重\"}]},\"resCode\":\"0000\",\"resMsg\":\"提交成功\"}";
        JSONObject mockReposne = JSON.parseObject(response);
        Report report = new Report();
        //report.setCustomerName("吴生根");
        //report.setCustomerIdCard("330726198110110935");
        Map<String, JSONObject> pool = new HashMap<>();
        pool.put("youfenCriminal", mockReposne);
        CriminalModuleStrategy strategy = new CriminalModuleStrategy(report, supplyAPIRepository, apiSearchRepository);
        strategy.fetchData(pool);
        CriminalModule module = (CriminalModule) strategy.analyseData();
        assertEquals(1, module.getCount().intValue());
        JSONArray criminals = module.getCriminals();
        assertEquals(1, criminals.size());
        Criminal criminal = ((JSONObject) criminals.get(0)).toJavaObject(Criminal.class);
        assertEquals("严重", criminal.getCaseLevel());
        assertEquals("5-10年（不含）以内", criminal.getCasePeriod());
        assertEquals("前科", criminal.getCaseSource());
        assertEquals("侵犯财产案", criminal.getCaseType());
        assertEquals("比中前科", criminal.getCrimeType());
        assertEquals(1, criminal.getCount().intValue());
    }
}
