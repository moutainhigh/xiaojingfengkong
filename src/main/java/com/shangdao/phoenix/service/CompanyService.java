package com.shangdao.phoenix.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.account.Account;
import com.shangdao.phoenix.entity.account.AccountRepository;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.company.Company.OrganizationEnum;
import com.shangdao.phoenix.entity.company.CompanyRepository;
import com.shangdao.phoenix.entity.companyApplyForm.CompanyApplyFormRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.role.RoleRepository;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.entity.user.User.Source;
import com.shangdao.phoenix.entity.user.UserRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.CommonUtils;
import com.shangdao.phoenix.util.OutsideRuntimeException;

@Service
public class CompanyService implements InterfaceEntityService {
    @Autowired
    private InitService initService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EntityManagerRepository entityManagerRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyApplyFormRepository companyApplyFormRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ActRepository actRepository;
    @Autowired
    private SmsService smsService;
    @Value("${salt}")
    private String salt;

    @PostConstruct
    public void init() {
        EntityManager companyEntityManager = entityManagerRepository.findByName("company");
        Act addAct = actRepository.findByEntityManagerIdAndCode(companyEntityManager.getId(), "addMember");
        if (addAct == null) {
            addAct = new Act(companyEntityManager,"添加成员","addMember");
            actRepository.save(addAct);
        }
        Act moveAct = actRepository.findByEntityManagerIdAndCode(companyEntityManager.getId(), "move");
        if (moveAct == null) {
            moveAct = new Act(companyEntityManager,"移交企业","move");
            actRepository.save(moveAct);
        }

        Act enableAct = actRepository.findByEntityManagerIdAndCode(companyEntityManager.getId(),"enable");
        if(enableAct == null){
            enableAct = new Act(companyEntityManager,"禁启用","enable");
            actRepository.save(enableAct);
        }
    }

    public void create(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Company post = (Company) postBody;

        Company existCompany = companyRepository.findByName(post.getName());
        if (existCompany != null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.COMPANY_NAME_EXIST);
        }

        if (!post.getFromBackend()) {   //前台创建公司
//            if (!smsService.checkSms(post.getManager().getUsername(), post.getSms())) {
//                throw new OutsideRuntimeException(ExceptionResultEnum.SMS_ERROR);
//            }

            User managerUser = userRepository.findByUsernameAndSource(post.getManager().getUsername(), Source.WEB);
            if(managerUser == null){
                managerUser = new User(post.getManager().getUsername(),post.getManager().getName());
                managerUser.setPassword(CommonUtils.MD5Encode("123456", salt));
            }
            Company parent = CommonUtils.currentUser().getUser().getCompany();
            Set<Role> roles = new HashSet<Role>();
            if (post.getOrganizationType().equals(OrganizationEnum.ENTERPRISE)) {
                roles.add(roleRepository.findByCode("ENTERPRISEMANAGER"));
                managerUser.setTopCompany(parent);
            } 
            if(OrganizationEnum.TEAM.equals(post.getOrganizationType())){
                roles.add(roleRepository.findByCode("TEAMMANAGER"));
                managerUser.setTopCompany(parent.getParent());
            }

            managerUser.setRoles(roles);
            userRepository.save(managerUser);
            post.setParent(parent);
            post.setDiscount(parent.getDiscount());
            post.setManager(managerUser);

        } else {//后台创建公司
          User managerUser = userRepository.findByUsernameAndSource(post.getManager().getUsername(), Source.WEB);
          if(managerUser == null){
              managerUser = new User(post.getManager().getUsername(),post.getManager().getName());
              managerUser.setPassword(CommonUtils.MD5Encode("123456", salt));
          }
          Set<Role> roles = new HashSet<Role>();
          if(OrganizationEnum.GROUP.equals(post.getOrganizationType())){
              roles.add(roleRepository.findByCode("GROUPMANAGER"));
          } 
          if(OrganizationEnum.ENTERPRISE.equals(post.getOrganizationType())){
              roles.add(roleRepository.findByCode("ENTERPRISEMANAGER"));
          }
          managerUser.setRoles(roles);
          userRepository.save(managerUser);
          
          post.setManager(managerUser);
          
        }
        
        Account account = new Account(entityManagerRepository,post.getName(),post.getOrganizationType());
        account = accountRepository.save(account);
        
        post.setAccount(account);
        post.setSearchTimes(0L);
        post.setEnabled(true);
        
    }

    // 把save之后的对象关联到用户
    public void afterCreate(PostMethodWrapper postMethodWrapper, Object newInstance) {
        Company company = (Company) newInstance;
        User manager = company.getManager();
        manager.setCompany(company);
        if(company.getParent() == null){
            manager.setTopCompany(company);
        }
        userRepository.save(manager);
    }

    public void update(PostMethodWrapper postMethodWrapper, Object postObject, Object oldInstance) {
        Company post = (Company) postObject;
        Company old = (Company) oldInstance;

        User managerUser = userRepository.findByUsernameAndSource(post.getManager().getUsername(),Source.WEB);
        if(!post.getManager().getUsername().equals(old.getManager().getUsername())){
            
            if(managerUser == null){
                managerUser = new User(post.getManager().getUsername(),post.getManager().getName());
                managerUser.setPassword(CommonUtils.MD5Encode("123456", salt));
                managerUser.setCompany(old);
            }
            if(managerUser.getCompany() != old){
                throw new OutsideRuntimeException(ExceptionResultEnum.USER_NOT_COMPANY);
            }
            
            User oldManagerUser = old.getManager();
            Set<Role> roles2= new HashSet<Role>();
            roles2.add(roleRepository.findByCode("GUEST"));
            oldManagerUser.setRoles(roles2 );
            userRepository.save(oldManagerUser);
            
        }
        
        Set<Role> roles = new HashSet<Role>();
        if(OrganizationEnum.GROUP.equals(post.getOrganizationType())){
            roles.add(roleRepository.findByCode("GROUPMANAGER"));
        } 
        if(OrganizationEnum.ENTERPRISE.equals(post.getOrganizationType())){
            roles.add(roleRepository.findByCode("ENTERPRISEMANAGER"));
        }
        managerUser.setRoles(roles);
        userRepository.save(managerUser);
        
        post.setManager(managerUser);
        
        old.getAccount().setRemainder(post.getAccount().getRemainder());
        accountRepository.save(old.getAccount());
        
    }

    // 添加成员
    public void addMenber(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Company post = (Company) postBody;
        Company old = (Company) oldInstance;
        if (!smsService.checkSms(post.getMemberMobile(), post.getSms())) {
            throw new OutsideRuntimeException(ExceptionResultEnum.SMS_ERROR);
        }
        User newMember = userRepository.findByUsernameAndSource(post.getMemberMobile(), Source.WEB);
        if (newMember == null) {
            throw new OutsideRuntimeException(ExceptionResultEnum.USER_NOT_REGISTER);
        }
        newMember.setCompany(old);
        userRepository.save(newMember);
    }

    // 移交企业
    public void move(PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance) {
        Company post = (Company) postBody;
        if (!smsService.checkSms(post.getManagerMobile(), post.getSms())) {
            throw new OutsideRuntimeException(ExceptionResultEnum.SMS_ERROR);
        }
        User newManager = userRepository.findByUsernameAndSource(post.getMemberMobile(), Source.WEB);
        Set<Role> roles = newManager.getRoles();
        newManager.setRoles(CommonUtils.currentUser().getUser().getRoles());
        User oldManager = CommonUtils.currentUser().getUser();
        oldManager.setRoles(roles);
        userRepository.save(newManager);
        userRepository.save(oldManager);
    }

    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(Company.class).setEntityService(this);

    }

}
