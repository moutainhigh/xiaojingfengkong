package com.shangdao.phoenix.entity.report.strategy;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.Application;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.module.CriminalModule;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanghengkun
 * @date 2018/04/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class CriminalModuleStrategyTest {

    @Autowired
    private SupplyAPIRepository supplyAPIRepository;

    @Autowired
    private APISearchRepository apiSearchRepository;

    @Test
    public void test() {
        Report report = new Report();
        report.setCustomerName("吴生根");
        report.setCustomerIdCard("330726198110110935");
        Map<String, JSONObject> pool = new HashMap<>();
        CriminalModuleStrategy strategy = new CriminalModuleStrategy(report, supplyAPIRepository, apiSearchRepository);
        strategy.fetchData(pool);
        CriminalModule module = (CriminalModule) strategy.analyseData();
        System.out.println(module);
    }
}
