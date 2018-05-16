package com.shangdao.phoenix.service;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.apisearch.APISearchRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.moduleManager.ModuleManager;
import com.shangdao.phoenix.entity.productionManager.ProductionManager;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.report.ReportRepository;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.strategy.BaseStrategy;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.entity.supplyAPI.SupplyAPIRepository;
import com.shangdao.phoenix.service.GetMethodService.GetMethodWrapper;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.thread.ModuleCallable;
import com.shangdao.phoenix.util.HTTPResponse;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huanghengkun
 * @date 2018/04/03
 */
@Service
public class ReportService implements InterfaceEntityService {
    @Autowired
    private InitService initService;

    @Autowired
    private SupplyAPIRepository supplyAPIRepository;

    @Autowired
    private APISearchRepository apiSearchRepository;

    @Autowired
    private ActRepository actRepository;

    @Autowired
    private EntityManagerRepository entityManagerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReportRepository reportRepository;

    private final static Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final static ExecutorService executorService = Executors.newFixedThreadPool(100);

    @PostConstruct
    public void init() {
        EntityManager entityManager = entityManagerRepository.findByName("report");
        if (actRepository.findByEntityManagerIdAndCode(entityManager.getId(), "start") == null) {
            Act startAct = new Act(entityManager, "开始查询", "start");
            actRepository.save(startAct);
        }
    }

    public void create(PostMethodService.PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        ProductionManager productionManager = (ProductionManager) postBody;
        productionManager.setEnabled(true);
    }

    public HTTPResponse detail(GetMethodWrapper getMethodWrapper, HTTPResponse response) {
        Map map = (Map) response.getData();
        long id = (long) map.get("id");
        Report report = reportRepository.findOne(id);
        ProductionManager productionManager = report.getProductionManager();
        Set<ModuleManager> moduleManagers = productionManager.getModuleManagers();
        for (ModuleManager moduleManager : moduleManagers) {
            String code = moduleManager.getCode();
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(report);
            try {
                BaseModule baseModule = (BaseModule) beanWrapper.getPropertyValue(code);
                map.put(code, baseModule);
            } catch (RuntimeException e) {
                logger.warn("moduleManager.code配置错误,moduleManager:" + moduleManager.getName() + ",code:" + code);
            }
        }
        return response;
    }

    public void start(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Report report = (Report) oldInstance;
        ProductionManager productionManager = report.getProductionManager();
        if (!productionManager.getEnabled()) {
            throw new OutsideRuntimeException(1263, "该产品已禁用,请联系管理员");
        }
        Set<ModuleManager> moduleManagers = productionManager.getModuleManagers();
        List<ModuleCallable> tasks = new ArrayList<>();
        Lock lock = new ReentrantLock();
        Map<String, JSONObject> pool = new ConcurrentHashMap<>();
        for (ModuleManager moduleManager : moduleManagers) {
            if (moduleManager.getEnabled()) {
                String strategyName = moduleManager.getStrategyName();
                String spareStrategyName = moduleManager.getSpareStrategyName();
                try {
                    BaseStrategy strategy = null;
                    BaseStrategy spareStrategy = null;
                    if (!StringUtils.isEmpty(strategyName)) {
                        Object o = Class.forName(strategyName).getConstructor(Report.class, SupplyAPIRepository.class, APISearchRepository.class)
                                .newInstance(report, supplyAPIRepository, apiSearchRepository);
                        if (o instanceof BaseStrategy) {
                            strategy = (BaseStrategy) o;
                        } else {
                            logger.warn("strategyName配置错误,非BaseStrategy子类,module:" + moduleManager.getName());
                        }
                    }
                    if (!StringUtils.isEmpty(spareStrategyName)) {
                        Object spareO = Class.forName(spareStrategyName).getConstructor(Report.class, SupplyAPIRepository.class, APISearchRepository.class)
                                .newInstance(report, supplyAPIRepository, apiSearchRepository);
                        if (spareO instanceof BaseStrategy) {
                            spareStrategy = (BaseStrategy) spareO;
                        } else {
                            logger.warn("spareStrategyName配置错误,非BaseStrategy子类,module:" + moduleManager.getName());
                        }
                    }
                    if (strategy == null && spareStrategy == null) {
                        logger.warn("strategyName和spareStrategyName都为null,配置错误,module:" + moduleManager.getName());
                    } else {
                        tasks.add(new ModuleCallable(strategy, spareStrategy, pool, lock, moduleManager.getDispalyOrder()));
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    logger.warn("strategyName||spareStrategyName配置错误,module:" + moduleManager.getName());
                }
            }
        }
        List<Future<BaseModule>> futures = new ArrayList<>();
        try {
            futures = executorService.invokeAll(tasks, 10L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("reportId:" + report.getId() + ",批查询中断:" + e.getMessage());
        }
        /*for (Future<BaseModule> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(Report.class).setEntityService(this);
    }
}
