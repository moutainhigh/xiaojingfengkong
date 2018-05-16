package com.shangdao.phoenix.entity.customer;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.report.Report;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

@Entity
@Table(name = "customer",indexes = {@Index(name = "idCard_company_id",columnList = "id_card,company_id",unique = true)})
public class Customer implements IBaseEntity{
    
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
    
    @Column(name = "id_card")
    private String idCard;
    
    @Column(name = "mobile")
    private String mobile;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToOne
    @JoinColumn(name = "last_report_id")
    private Report lastReport;
    
    @OneToMany(mappedBy = "customer")
    private Set<Report> reports;
    
    @OneToMany(mappedBy = "customer")
    private Set<Evaluation> evaluations;
    
    

    public Report getLastReport() {
        return lastReport;
    }

    public void setLastReport(Report lastReport) {
        this.lastReport = lastReport;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    

}
