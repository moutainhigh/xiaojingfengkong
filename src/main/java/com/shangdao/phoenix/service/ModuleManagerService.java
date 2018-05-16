package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.moduleManager.ModuleManager;
import com.shangdao.phoenix.entity.moduleManager.ModuleManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author huanghengkun
 * @date 2018/04/25
 */
@Service
public class ModuleManagerService implements InterfaceEntityService {

    @Autowired
    private InitService initService;

    @Autowired
    private ModuleManagerRepository moduleManagerRepository;

    @PostConstruct
    public void init() {
        if (moduleManagerRepository.count() == 0) {
            ModuleManager basicInfoModule = new ModuleManager(1, "基础信息", 0.007, "com.shangdao.phoenix.entity.report.strategy.BasicInfoModuleStrategy", "basicInfoModule");

            ModuleManager idCheckModule = new ModuleManager(2, "身份校验", 0.15, "com.shangdao.phoenix.entity.report.strategy.IdCheckModuleStrategy", "idCheckModule");

            ModuleManager educationModule = new ModuleManager(3, "学历信息", 2.3, "com.shangdao.phoenix.entity.report.strategy.EducationModuleStrategy", "educationModule");

            ModuleManager zhimaScoreModule = new ModuleManager(4, "芝麻信用", 0D, "com.shangdao.phoenix.entity.report.strategy.ZhimaScoreModuleStrategy", "zhimaScoreModule");

            ModuleManager multipleHeadLendModule = new ModuleManager(5, "多头借贷", 1.2, "com.shangdao.phoenix.entity.report.strategy.MultipleHeadLendModuleStrategy", "multipleHeadLendModule");

            ModuleManager overdueCreditModule = new ModuleManager(6, "信贷逾期", 1.2, "com.shangdao.phoenix.entity.report.strategy.OverdueCreditModuleStrategy", "overdueCreditModule");

            ModuleManager fraudModule = new ModuleManager(7, "潜在风险", 1.2, "com.shangdao.phoenix.entity.report.strategy.FraudModuleStrategy", "fraudModule");

            ModuleManager criminalModule = new ModuleManager(8, "刑事犯罪", 1.3, "com.shangdao.phoenix.entity.report.strategy.CriminalModuleStrategy", "criminalModule");

            ModuleManager dishonestBlackModule = new ModuleManager(9, "法院失信", 0.01, "com.shangdao.phoenix.entity.report.strategy.DishonestBlackModuleStrategy", "dishonestBlackModule");

            ModuleManager courtRulesModule = new ModuleManager(10, "法院判决", 2.4, "com.shangdao.phoenix.entity.report.strategy.CourtRuledStrategy", "courtRuledMoudle");

            ModuleManager violationModule = new ModuleManager(11, "车辆违章", 0.136, "com.shangdao.phoenix.entity.report.strategy.ViolationXKModuleStrategy", "violationModule");
            violationModule.setSpareStrategyName("com.shangdao.phoenix.entity.report.strategy.ViolationWSModuleStrategy");

            moduleManagerRepository.save(basicInfoModule);
            moduleManagerRepository.save(idCheckModule);
            moduleManagerRepository.save(educationModule);
            moduleManagerRepository.save(zhimaScoreModule);
            moduleManagerRepository.save(multipleHeadLendModule);
            moduleManagerRepository.save(overdueCreditModule);
            moduleManagerRepository.save(fraudModule);
            moduleManagerRepository.save(criminalModule);
            moduleManagerRepository.save(dishonestBlackModule);
            moduleManagerRepository.save(courtRulesModule);
            moduleManagerRepository.save(violationModule);
        }
    }

    public void create(PostMethodService.PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        ModuleManager moduleManager = (ModuleManager) postBody;
        moduleManager.setEnabled(true);
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(ModuleManager.class).setEntityService(this);
    }
}
