package com.shangdao.phoenix.entity.companybill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.account.Account;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;
import com.shangdao.phoenix.util.CommonUtils;
import com.shangdao.phoenix.util.DateUtils;
import com.shangdao.phoenix.util.ReportUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "company_bill")
public class CompanyBill implements ILogEntity<CompanyBillLog, CompanyBillFile, CompanyBillNotice> {
    @Transient
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;

    @Column(name = "name" ,unique = true)
    private String name;

    @Column(name = "deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Transient
    private List<OssImage> uploadFiles;

    @Transient
    private String note;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyBillLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyBillFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyBillNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @Column(name = "amount")
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BillTypeEnum type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatusEnum status;
    
    @Column(name = "bill_no")
    private String billNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "plat_form")
    private PlatformEnum platform;

    @Column(name = "comment")
    private  String comment;

    public CompanyBill(){

    }

    public CompanyBill(EntityManagerRepository repository, Account account,PlatformEnum platform, Company Company, BillTypeEnum billType,String note){
        this.amount = account.getPayAmount();
        this.company = Company;
        this.createdAt = new Date();
        this.createdBy = CommonUtils.currentUser().getUser();
        this.entityManager = repository.findByName("companyBill");
        this.platform = platform;
        this.type = billType;
        this.billNo = ReportUtils.createReportNo();
        this.comment = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BillTypeEnum getType() {
        return type;
    }

    public void setType(BillTypeEnum type) {
        this.type = type;
    }

    public BillStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BillStatusEnum status) {
        this.status = status;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
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
    public List<OssImage> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<OssImage> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    @Override
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public Set<CompanyBillLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<CompanyBillLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<CompanyBillFile> getFiles() {
        return files;
    }

    public void setFiles(Set<CompanyBillFile> files) {
        this.files = files;
    }

    @Override
    public Set<CompanyBillNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<CompanyBillNotice> notices) {
        this.notices = notices;
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

    @Override
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
    
    /**
     * 账单类型
     * 
     * @author Administrator
     *
     */
    public enum BillTypeEnum {

        PCDEPOSIT("客户充值"),

        BACKDEPOSIT("后台充值"),

        WITHDRAW("查询扣款"),

        CONSUME("后台扣款");

        private String message;

        BillTypeEnum(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * 账单状态
     * 
     * @author Administrator
     *
     */
    public enum BillStatusEnum {

        WAIT_BUYER_PAY, // 等待支付

        TRADE_CLOSED,   // 交易关闭

        TRADE_SUCCESS   // 支付成功
    }

    /**
     * 支付平台
     * 
     * @author Administrator
     *
     */
    public enum PlatformEnum {
        ALIPAY, // 支付宝
        BACKEND, //后台系统
        WEIXIN  //微信
    }
}