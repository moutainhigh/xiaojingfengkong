package com.shangdao.phoenix.entity.companyApplyForm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.company.Company.IndustryTypeEnum;
import com.shangdao.phoenix.entity.company.Company.OrganizationEnum;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author duyiting
 * @date 2018/03/30
 */

@Entity
@Table(name = "company_apply_form")
public class CompanyApplyForm implements ILogEntity<CompanyApplyFormLog,CompanyApplyFormFile,CompanyApplyFormNotice> {
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

    @Transient
    private List<OssImage> uploadFiles;

    @Transient
    private String note;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyApplyFormLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyApplyFormFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<CompanyApplyFormNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_type")
    private Company.LicenseTypeEnum licenseType;

    @Column(name = "license_number")
    private String licenseNumber;
    
    @ManyToOne
    @JoinColumn(name = "verified_by_id")
    private User verifiedBy;

    @Column(name = "verified_at")
    private Date verifiedAt;

    @Column(name = "verified_result")
    private Boolean verifiedResult;

    @Column(name = "reason")
    private String reason;
    
    @OneToOne(mappedBy = "companyApplyForm")
    private Company company;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "industry_type")
    private IndustryTypeEnum industryType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type")
    private OrganizationEnum organizationType;
    
    @Column(name = "discount")
    private Integer discount;
    

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public IndustryTypeEnum getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryTypeEnum industryType) {
        this.industryType = industryType;
    }

    public OrganizationEnum getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationEnum organizationType) {
        this.organizationType = organizationType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Date verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Boolean getVerifiedResult() {
        return verifiedResult;
    }

    public void setVerifiedResult(Boolean verifiedResult) {
        this.verifiedResult = verifiedResult;
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
    public Set<CompanyApplyFormLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<CompanyApplyFormLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<CompanyApplyFormFile> getFiles() {
        return files;
    }

    public void setFiles(Set<CompanyApplyFormFile> files) {
        this.files = files;
    }

    @Override
    public Set<CompanyApplyFormNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<CompanyApplyFormNotice> notices) {
        this.notices = notices;
    }

    @Override
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
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

    public Company.LicenseTypeEnum getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Company.LicenseTypeEnum licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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
}
