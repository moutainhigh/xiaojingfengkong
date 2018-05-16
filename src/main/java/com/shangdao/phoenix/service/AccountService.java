package com.shangdao.phoenix.service;

import com.shangdao.phoenix.entity.account.Account;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActRepository;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.companybill.CompanyBillRepository;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.enums.ExceptionResultEnum;
import com.shangdao.phoenix.util.OutsideRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangdao.phoenix.entity.companybill.CompanyBill;

import javax.annotation.PostConstruct;

@Service
public class AccountService implements InterfaceEntityService{
    
    @Autowired
    private InitService initService;
    @Autowired
    private EntityManagerRepository entityManagerRepository;
    @Autowired
    private ActRepository actRepository;
    @Autowired
    private CompanyBillRepository companyBillRepository;

    @PostConstruct
    public void init(){
        EntityManager accountManager = entityManagerRepository.findByName("account");
        Act payAct = actRepository.findByEntityManagerIdAndCode(accountManager.getId(),"pay");
        if(payAct == null){
            payAct = new Act(accountManager,"充值","pay");
            actRepository.save(payAct);
        }

        Act deductAct = actRepository.findByEntityManagerIdAndCode(accountManager.getId(),"deduct");
        if(deductAct == null){
            deductAct = new Act(accountManager,"扣款","deduct");
            actRepository.save(deductAct);
        }

    }

    //客户充值和后台充值
    public void pay(PostMethodService.PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance){
        Account post = (Account) postBody;
        Account old = (Account) oldInstance;
        CompanyBill companyBill = null;
        if(post.getPayType() == null){
            throw new OutsideRuntimeException(ExceptionResultEnum.UNKNOW_PAY_TYPE);
        }
        if(post.getPayAmount() == null || post.getPayAmount().equals(0d)){
            throw new OutsideRuntimeException(ExceptionResultEnum.HAVE_NOT_PAY_AMOUNT);
        }
        if(CompanyBill.BillTypeEnum.BACKDEPOSIT.equals(post.getPayType())){
            old.deposit(0,post.getPayAmount());
            companyBill = new CompanyBill(entityManagerRepository,post,CompanyBill.PlatformEnum.BACKEND,old.getCompany(),CompanyBill.BillTypeEnum.BACKDEPOSIT,post.getNote());
        }
        if(CompanyBill.BillTypeEnum.PCDEPOSIT.equals(post.getPayType())){
            old.deposit(post.getPayAmount(),post.getGiveAmount());
            companyBill = new CompanyBill(entityManagerRepository,post,CompanyBill.PlatformEnum.ALIPAY,old.getCompany(),CompanyBill.BillTypeEnum.PCDEPOSIT,null);
        }
        companyBillRepository.save(companyBill);

    }

    //查询扣款和后台扣款
    public void deduct(PostMethodService.PostMethodWrapper postMethodWrapper, Object postBody, Object oldInstance){
        Account post = (Account) postBody;
        Account old = (Account) oldInstance;
        CompanyBill companyBill = null;
        if(post.getPayType() == null){
            throw new OutsideRuntimeException(ExceptionResultEnum.UNKNOW_PAY_TYPE);
        }
        if(post.getPayAmount() == null || post.getPayAmount().equals(0d)){
            throw new OutsideRuntimeException(ExceptionResultEnum.HAVE_NOT_PAY_AMOUNT);
        }
        if(post.getPayAmount() > old.getRemainder()){
            throw new OutsideRuntimeException(ExceptionResultEnum.ACCOUNT_NO_MONEY);
        }
        if(CompanyBill.BillTypeEnum.CONSUME.equals(post.getPayType()) && post.getPayAmount() > old.getGiveRemainder()){
            throw new OutsideRuntimeException(ExceptionResultEnum.GIVE_ACCOUNT_NO_MONEY);
        }
        if(CompanyBill.BillTypeEnum.WITHDRAW.equals(post.getPayType())){
            old.consume(post.getPayAmount(),post.getPayType());
            companyBill = new CompanyBill(entityManagerRepository,post,null,old.getCompany(),CompanyBill.BillTypeEnum.WITHDRAW,null);
        }
        if(CompanyBill.BillTypeEnum.CONSUME.equals(post.getPayType())){
            old.consume(post.getPayAmount(),post.getPayType());
            companyBill = new CompanyBill(entityManagerRepository,post,null,old.getCompany(),CompanyBill.BillTypeEnum.CONSUME,post.getNote());
        }
        companyBillRepository.save(companyBill);
    }


    @Override
    @PostConstruct
    public void registerService() {
        initService.getStructure(Account.class).setEntityService(this);
        
    }

}
