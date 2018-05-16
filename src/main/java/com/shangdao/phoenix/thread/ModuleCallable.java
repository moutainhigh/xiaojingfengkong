package com.shangdao.phoenix.thread;

import com.alibaba.fastjson.JSONObject;
import com.shangdao.phoenix.entity.report.module.BaseModule;
import com.shangdao.phoenix.entity.report.strategy.BaseStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

/**
 * @author huanghengkun
 * @date 2018/04/03
 */
public class ModuleCallable implements Callable<BaseModule> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleCallable.class);

    private BaseStrategy strategy;
    private BaseStrategy spareStrategy;
    private Map<String, JSONObject> pool;
    private final Lock lock;
    private Integer displayOrder;

    public ModuleCallable(BaseStrategy strategy, BaseStrategy spareStrategy, Map<String, JSONObject> pool, Lock lock, Integer displayOrder) {
        this.strategy = strategy;
        this.spareStrategy = spareStrategy;
        this.pool = pool;
        this.lock = lock;
        this.displayOrder = displayOrder;
    }

    @Override
    public BaseModule call() throws Exception {
        BaseModule result = null;
        if (strategy != null && strategy.isAPIActive()) {
            boolean isWaitForOther;
            do {
                lock.lock();
                isWaitForOther = strategy.isContainsAPI(pool) && strategy.isAPIUnfinished(pool);
                if (isWaitForOther) {
                    lock.unlock();
                    try {
                        logger.info(strategy.getClass().getSimpleName() + "其他线程在查询主接口,休眠1s");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        logger.warn("线程发生中断,strategy:" + strategy.getClass().getSimpleName());
                        Thread.currentThread().interrupt();
                    }
                } else {
                    strategy.tryPutEmptyAPI(pool);
                    lock.unlock();
                }
            } while (isWaitForOther);

            try {
                if (strategy.fetchData(pool)) {
                    strategy.putAPIResponseIntoPool(pool);
                } else {
                    strategy.removeEmptyAPI(pool);
                }
            } catch (Exception e) {
                //如果发生异常的话,从pool里移出空的api
                strategy.removeEmptyAPI(pool);
            }
            result = strategy.analyseData();
            if (result != null) {
                result.setDisplaySort(displayOrder);
                strategy.setModuleIntoReport(result);
            }
        }


        // 如果结果为null且备用接口可用
        if (spareStrategy != null && result == null && spareStrategy.isAPIActive()) {
            boolean isWaitForOther;
            do {
                lock.lock();
                isWaitForOther = spareStrategy.isContainsAPI(pool) && !spareStrategy.isAPIUnfinished(pool);
                if (isWaitForOther) {
                    lock.unlock();
                    try {
                        logger.info(spareStrategy.getClass().getSimpleName() + "其他线程在查询备用接口,休眠1s");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        logger.warn("线程发生中断,strategy:" + strategy.getClass().getSimpleName());
                        Thread.currentThread().interrupt();
                    }
                } else {
                    spareStrategy.tryPutEmptyAPI(pool);
                    lock.unlock();
                }
            } while (isWaitForOther);

            try {
                if (spareStrategy.fetchData(pool)) {
                    spareStrategy.putAPIResponseIntoPool(pool);
                } else {
                    spareStrategy.removeEmptyAPI(pool);
                }
            } catch (Exception e) {
                //如果发生异常的话,从pool里移出空的api
                spareStrategy.removeEmptyAPI(pool);
            }
            result = spareStrategy.analyseData();
            if (result != null) {
                result.setDisplaySort(displayOrder);
                spareStrategy.setModuleIntoReport(result);
            }
        }

        return result;
    }
}
