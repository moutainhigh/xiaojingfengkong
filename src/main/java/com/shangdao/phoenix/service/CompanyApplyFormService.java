package com.shangdao.phoenix.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.account.AccountRepository;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.company.Company.OrganizationEnum;
import com.shangdao.phoenix.entity.company.CompanyRepository;
import com.shangdao.phoenix.entity.companyApplyForm.CompanyApplyForm;
import com.shangdao.phoenix.entity.companyApplyForm.CompanyApplyFormRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.state.StateRepository;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.entity.user.UserRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.CommonUtils;
import com.shangdao.phoenix.util.OutsideRuntimeException;

@Service
public class CompanyApplyFormService implements InterfaceEntityService {

    @Autowired
    private InitService initService;
    @Autowired
    private EntityManagerRepository entityManagerRepository;
    @Autowired
    private ActRepository actRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CompanyApplyFormRepository companyApplyFormRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        EntityManager formEntityManager = entityManagerRepository.findByName("companyApplyForm");
        Act verifyAct = actRepository.findByEntityManagerIdAndCode(formEntityManager.getId(), "verify");
        if (verifyAct == null) {
            verifyAct = new Act(formEntityManager,"审核","verify");
            actRepository.save(verifyAct);
        }

        State state = stateRepository.findByEntityManagerIdAndCode(formEntityManager.getId(), "NOPASS");
        if (state == null) {
            state = new State(formEntityManager,"未通过","NOPASS");
            stateRepository.save(state);
        }
    }

    public void create(PostMethodWrapper postMethodWrapper, Object postBody, Object oldIntance) {
        CompanyApplyForm post = (CompanyApplyForm) postBody;

        Company existCompany = companyRepository.findByName(post.getName());
        if (existCompany != null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.COMPANY_NAME_EXIST);
        }

        long countName = companyApplyFormRepository.countByNameAndState(post.getName(), stateRepository
                .findByEntityManagerIdAndCode(postMethodWrapper.getStructure().getEntityManagerId(), "CREATER"));

        if (countName > 0L) {
            throw new OutsideRuntimeException(ExceptionResultEnum.SOME_ONE_USER_NAME);
        }

        long countNameCreated = companyApplyFormRepository.countByCreatedByIdAndState(
                CommonUtils.currentUser().getUser().getId(), stateRepository.findByEntityManagerIdAndCode(
                        postMethodWrapper.getStructure().getEntityManagerId(), "CREATED"));
        if (countNameCreated > 0L) {
            throw new OutsideRuntimeException(ExceptionResultEnum.APPLY_FORM_EXIST);
        }
        long countByNameFinished = companyApplyFormRepository.countByCreatedByIdAndState(
                CommonUtils.currentUser().getUser().getId(), stateRepository.findByEntityManagerIdAndCode(
                        postMethodWrapper.getStructure().getEntityManagerId(), "FINISHED"));
        if (countByNameFinished > 0L) {
            throw new OutsideRuntimeException(ExceptionResultEnum.YOU_HAVE_COMPANY);
        }
    }

    public void verify(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        CompanyApplyForm post = (CompanyApplyForm) postBody;
        CompanyApplyForm old = (CompanyApplyForm) oldInstance;
        post.setVerifiedAt(new Date());
        post.setVerifiedBy(CommonUtils.currentUser().getUser());
        if (post.getDiscount() == null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.DISCOUNT_NOT_HAVE);
        }
        if (post.getVerifiedResult()) {
            post.setState(stateRepository
                    .findByEntityManagerIdAndCode(postMethodWrapper.getStructure().getEntityManagerId(), "FINISHED"));
            User user = old.getCreatedBy();
            Company company = new Company(old, postMethodWrapper, entityManagerRepository, stateRepository,
                    accountRepository);
            companyRepository.save(company);
            user.setCompany(company);
            user.setTopCompany(company);
            Set<Role> roles = new HashSet<Role>();
            if (old.getOrganizationType().equals(OrganizationEnum.GROUP)) {
                roles.add(roleRepository.findByCode("GROUPMANAGER"));
            } else {
                roles.add(roleRepository.findByCode("ENTERPRISEMANAGER"));
            }
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            if (StringUtils.isBlank(post.getReason())) {
                throw new OutsideRuntimeException(ExceptionResultEnum.INPUT_REFUSR_REASON);
            }
            post.setState(stateRepository
                    .findByEntityManagerIdAndCode(postMethodWrapper.getStructure().getEntityManagerId(), "NOPASS"));
        }

    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(CompanyApplyForm.class).setEntityService(this);

    }

}
