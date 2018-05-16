package com.shangdao.phoenix.entity.account;

import java.util.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.companybill.CompanyBill;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

@Entity
@Table(name = "account")
public class Account implements IBaseEntity{
    
    /**
     * 
     */
    @Transient
    public static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;

    @Column(name = "name")
    private String name;
    
    @Column(name = "deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;
    
    @Column(name = "remainder")
    private Double  remainder;     //当前总余额
    
    @Column(name = "pay_amount")
    private Double  payRemainder;    //当前充值剩余
    
    @Column(name = "give_amount")
    private Double  giveRemainder;   //当前赠送剩余
    
    @Column(name = "history_pay")
    private Double historyPay;     //历史充值
    
    @Column(name = "history_give")
    private Double historyGive;    //历史赠送
    
    @OneToOne(mappedBy = "account")
    private Company company;

    @OneToOne(mappedBy = "account")
    private User user;

    @Enumerated(EnumType.STRING)
    @Transient
    private CompanyBill.BillTypeEnum payType;

    @Enumerated(EnumType.STRING)
    @Transient
    private CompanyBill.PlatformEnum platType;

    @Transient
    private Double payAmount;   //当前充值或者扣钱金额

    @Transient
    private Double giveAmount;  //当前充值赠送金额

    @Transient
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private Company.OrganizationEnum accountType;

    public Account(){

    }
    public Account(EntityManagerRepository repository,String name,Company.OrganizationEnum type){
        this.remainder = 0d;
        this.giveRemainder = 0d;
        this.payRemainder = 0d;
        this.historyGive = 0d;
        this.historyPay = 0d;
        this.createdAt = new Date();
        this.name = name;
        this.entityManager = repository.findByName("account");
        this.accountType = type;
    }

    public void deposit(double pay, double give) {
        double absPay = Math.abs(pay);
        double absGive = Math.abs(give);
        remainder += (absPay+absGive);
        payRemainder += absPay;
        giveRemainder += absGive;
        historyPay += absPay;
        historyGive += absGive;
    }

    public void consume (double value, CompanyBill.BillTypeEnum typeEnum) {
        double absValue = Math.abs(value);
        remainder -= absValue;
        if(CompanyBill.BillTypeEnum.CONSUME.equals(typeEnum)){
            giveRemainder -= absValue;
            historyGive -= absValue;
        }else{
            if(payRemainder >= absValue){
                payRemainder -= absValue;
            }else{
                payRemainder = 0d;
                giveRemainder -= (absValue-payRemainder);
            }
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CompanyBill.PlatformEnum getPlatType() {
        return platType;
    }

    public void setPlatType(CompanyBill.PlatformEnum platType) {
        this.platType = platType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company.OrganizationEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(Company.OrganizationEnum accountType) {
        this.accountType = accountType;
    }

    public Double getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(Double giveAmount) {
        this.giveAmount = giveAmount;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public CompanyBill.BillTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(CompanyBill.BillTypeEnum payType) {
        this.payType = payType;
    }

    @Override
    public long getId() {
        return id;
    }
    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Double getRemainder() {
        return remainder;
    }

    public void setRemainder(Double remainder) {
        this.remainder = remainder;
    }

    public Double getPayRemainder() {
        return payRemainder;
    }

    public void setPayRemainder(Double payRemainder) {
        this.payRemainder = payRemainder;
    }

    public Double getGiveRemainder() {
        return giveRemainder;
    }

    public void setGiveRemainder(Double giveRemainder) {
        this.giveRemainder = giveRemainder;
    }

    public Double getHistoryPay() {
        return historyPay;
    }

    public void setHistoryPay(Double historyPay) {
        this.historyPay = historyPay;
    }

    public Double getHistoryGive() {
        return historyGive;
    }

    public void setHistoryGive(Double historyGive) {
        this.historyGive = historyGive;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
