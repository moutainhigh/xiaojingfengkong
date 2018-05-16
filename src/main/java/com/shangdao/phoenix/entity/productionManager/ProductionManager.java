package com.shangdao.phoenix.entity.productionManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.company.Company;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.moduleManager.ModuleManager;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author huanghengkun
 * @date 2018/04/02
 */
@Entity
@Table(name = "production_manager")
public class ProductionManager implements ILogEntity<ProductionManagerLog, ProductionManagerFile, ProductionManagerNotice> {
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
    private Set<ProductionManagerLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<ProductionManagerFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<ProductionManagerNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

    ///////////////////////////////////////////////////////////////////

    @Column(name = "price")
    private Double price;

    @ManyToMany
    @JoinTable(name = "production_module", joinColumns = {@JoinColumn(name = "production_manager_id")}, inverseJoinColumns = {@JoinColumn(name = "module_manager_id")})
    private Set<ModuleManager> moduleManagers;

    @ManyToMany(mappedBy = "productionManagers")
    private Set<Company> companies;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "enabled")
    private Boolean enabled;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<ModuleManager> getModuleManagers() {
        return moduleManagers;
    }

    public void setModuleManagers(Set<ModuleManager> moduleManagers) {
        this.moduleManagers = moduleManagers;
    }


    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
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
    public Set<ProductionManagerLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<ProductionManagerLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<ProductionManagerFile> getFiles() {
        return files;
    }

    public void setFiles(Set<ProductionManagerFile> files) {
        this.files = files;
    }

    @Override
    public Set<ProductionManagerNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<ProductionManagerNotice> notices) {
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
