package com.shangdao.phoenix.entity.company;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.account.Account;
import com.shangdao.phoenix.entity.account.AccountRepository;
import com.shangdao.phoenix.entity.companyApplyForm.CompanyApplyForm;
import com.shangdao.phoenix.entity.companybill.CompanyBill;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.entityManager.EntityManagerRepository;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.productionManager.ProductionManager;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.state.StateRepository;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;
import com.shangdao.phoenix.service.PostMethodService.PostMethodWrapper;
import com.shangdao.phoenix.util.TreeBuilder.TreeNode;

@Entity
@Table(name = "company")
public class Company implements ILogEntity<CompanyLog, CompanyFile, CompanyNotice>, TreeNode<Company> {

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

    @Column(name = "name", unique = true)
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
    private Set<CompanyLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

    @OneToMany(mappedBy = "company")
    private Set<User> employees;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Company parent;

    @OneToMany(mappedBy = "parent")
    private Set<Company> childrens;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type")
    private LicenseTypeEnum licenseType;

    @Column(name = "license_number")
    private String licenseNumber;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private IndustryTypeEnum industryType;

    @Column(name = "discount")
    private Integer discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type")
    private OrganizationEnum organizationType;

    @ManyToMany
    @JoinTable(name = "company_product", joinColumns = { @JoinColumn(name = "company_id") }, inverseJoinColumns = {
            @JoinColumn(name = "product_id") })
    private Set<ProductionManager> productionManagers;

    @OneToOne
    @JoinColumn(name = "company_apply_form_id")
    private CompanyApplyForm companyApplyForm;

    @Transient
    private Boolean fromBackend;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "search_times")
    private Long searchTimes;

    @OneToMany(mappedBy = "company")
    private Set<CompanyBill> companyBills;

    @Transient
    private String sms;

    @Transient
    private String memberMobile;

    @Transient
    private String memberName;

    @Transient
    private String managerMobile;

    public Company() {

    }

    public Company(CompanyApplyForm form, PostMethodWrapper postMethodWrapper, EntityManagerRepository repository,
            StateRepository stateRepository, AccountRepository accountRepository) {
        this.name = form.getName();
        this.address = form.getAddress();
        this.city = form.getCity();
        this.createdAt = new Date();
        this.createdBy = form.getCreatedBy();
        this.entityManager = repository.findByName("company");
        this.licenseType = form.getLicenseType();
        this.province = form.getProvince();
        this.licenseNumber = form.getLicenseNumber();
        this.companyApplyForm = form;
        this.industryType = form.getIndustryType();
        this.manager = form.getCreatedBy();
        this.organizationType = form.getOrganizationType();
        this.account = createAccount(this, repository, accountRepository);
        this.state = stateRepository.findByEntityManagerIdAndCode(this.entityManager.getId(), "CREATED");
        this.discount = form.getDiscount();
        this.enabled = true;
        this.searchTimes = 0L;
    }

    private Account createAccount(Company company, EntityManagerRepository repository,
            AccountRepository accountRepository) {
        Account account = new Account(repository,company.getName(),company.getOrganizationType());
        account = accountRepository.save(account);
        return account;
    }

    public Set<CompanyBill> getCompanyBills() {
        return companyBills;
    }

    public void setCompanyBills(Set<CompanyBill> companyBills) {
        this.companyBills = companyBills;
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
    public Set<CompanyLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<CompanyLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<CompanyFile> getFiles() {
        return files;
    }

    public void setFiles(Set<CompanyFile> files) {
        this.files = files;
    }

    @Override
    public Set<CompanyNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<CompanyNotice> notices) {
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

    public Set<User> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<User> employees) {
        this.employees = employees;
    }

    @Override
    public Company getParent() {
        return parent;
    }

    public void setParent(Company parent) {
        this.parent = parent;
    }

    public Set<Company> getChildrens() {
        return childrens;
    }

    public void setChildrens(Set<Company> childrens) {
        this.childrens = childrens;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LicenseTypeEnum getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseTypeEnum licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public IndustryTypeEnum getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryTypeEnum industryType) {
        this.industryType = industryType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public OrganizationEnum getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationEnum organizationType) {
        this.organizationType = organizationType;
    }

    public Set<ProductionManager> getProductionManagers() {
        return productionManagers;
    }

    public void setProductionManagers(Set<ProductionManager> productionManagers) {
        this.productionManagers = productionManagers;
    }

    public CompanyApplyForm getCompanyApplyForm() {
        return companyApplyForm;
    }

    public void setCompanyApplyForm(CompanyApplyForm companyApplyForm) {
        this.companyApplyForm = companyApplyForm;
    }

    public Boolean getFromBackend() {
        return fromBackend;
    }

    public void setFromBackend(Boolean fromBackend) {
        this.fromBackend = fromBackend;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getSearchTimes() {
        return searchTimes;
    }

    public void setSearchTimes(Long searchTimes) {
        this.searchTimes = searchTimes;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }

    public enum LicenseTypeEnum {
        /**
         * 普通营业执照
         */
        GENERAL,
        /**
         * 多证合一营业执照
         */
        MULTIPLE
    }

    public enum IndustryTypeEnum {
        FINANCE("金融行业"), LEASE("租赁行业"), INTERMEDIARIES("中介服务"), TALENTRECRUITMENT("人才招聘"), OTHERS("其他行业");

        private String message;

        IndustryTypeEnum(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum OrganizationEnum {
        /**
         * 集团
         */
        GROUP,
        /**
         * 企业
         */
        ENTERPRISE,
        /**
         * 团队
         */
        TEAM,
        /**
         * 个人
         */
        PERSON
    }

}
