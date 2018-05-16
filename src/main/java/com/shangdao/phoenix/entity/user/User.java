package com.shangdao.phoenix.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.account.Account;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.companybill.CompanyBill;
import com.shangdao.phoenix.entity.department.Department;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.message.Message;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.util.CommonUtils;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user", indexes = {@Index(name = "username_source", columnList = "username,source", unique = true)})
public class User implements IBaseEntity, Serializable {
    /**
     *
     */
    @Transient
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "country")
    private String country;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "province")
    private String province;

    @ManyToOne
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @Column(name = "username")
    @Length(min = 1, max = 64)
    @NotNull
    private String username;

    @Column(name = "name")
    @Length(min = 1, max = 64)
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "password")
    @Length(min = 6, max = 32)
    private String password;

    @Transient
    @Length(min = 6, max = 32)
    private String password2;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "is_leader")
    private Boolean leader;

    @Column(name = "position")
    private String position;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "english_name")
    private String englishName;
    
    @Column(name = "last_login_at")
    private Date lastLoginAt;
    
    @Column(name = "last_login_ip")
    private String lastLoginIp;
    
    @Column(name = "backend")
    private Boolean backend;
    @Transient
    private String sms;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    /**
     * 企业微信的状态
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(name = "user_department", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "department_id")})
    private Set<Department> departments;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @Column(name = "deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Source source;

    @OneToMany(mappedBy = "toUser")
    private Set<Message> receivedMessages;

    @OneToMany(mappedBy = "fromUser")
    private Set<Message> sentMessages;

    @Column(name = "not_read_message")
    private Integer notReadMessage;
    
    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name="report_count")
    private Long reportCount;

    @ManyToOne
    @JoinColumn(name="top_company_id")
    private Company topCompany;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "createdBy")
    private Set<CompanyBill> companyBills;
    
    public User(){
        
    }
    
    public User(String username,String name){
        this.username = username;
        this.name = name;
        this.createdAt = new Date();
        this.createdBy = CommonUtils.currentUser().getUser();
        this.source = Source.WEB;
        this.backend = false;
        this.enabled = true;
        this.notReadMessage = 0;
        this.reportCount = 0L;
    }


    public Account getAccount() {
        return account;
    }

    public Set<CompanyBill> getCompanyBills() {
        return companyBills;
    }

    public void setCompanyBills(Set<CompanyBill> companyBills) {
        this.companyBills = companyBills;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLeader() {
        return leader;
    }

    public void setLeader(Boolean leader) {
        this.leader = leader;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Boolean getBackend() {
        return backend;
    }

    public void setBackend(Boolean backend) {
        this.backend = backend;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Integer getNotReadMessage() {
        return notReadMessage;
    }

    public void setNotReadMessage(Integer notReadMessage) {
        this.notReadMessage = notReadMessage;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getReportCount() {
        return reportCount;
    }

    public void setReportCount(Long reportCount) {
        this.reportCount = reportCount;
    }

    public Company getTopCompany() {
        return topCompany;
    }

    public void setTopCompany(Company topCompany) {
        this.topCompany = topCompany;
    }

    /**
     * 用户性别枚举
     * @author Administrator
     *
     */
    public enum Gender {
        MALE, FEMALE
    }
    
    /**
     * 用户状态枚举 来自企业微信
     * @author Administrator
     *
     */
    public enum Status {
        //1=已激活，2=已禁用，4=未激活
        ACTIVE, INACTIVE, FORBIDDEN
    }
    

    @Override
    public String toString() {
        return "User [id=" + id + ", entityManager=" + entityManager + ", createdAt=" + createdAt + ", username="
                + username + ", name=" + name + ", mobile=" + mobile + ", password=" + password + ", password2="
                + password2 + ", gender=" + gender + ", email=" + email + ", leader=" + leader + ", position="
                + position + ", avatar=" + avatar + ", telephone=" + telephone + ", englishName=" + englishName
                + ", status=" + status + ", departments=" + departments + ", roles=" + roles + ", deletedAt="
                + deletedAt + ", createdBy=" + createdBy + ", state=" + state + "]";
    }
    
    /**
     * 用户来源枚举
     * @author Administrator
     *
     */
    public enum Source {
        APP, WXPUBLIC, WXWORK, WEB, API
    }


}