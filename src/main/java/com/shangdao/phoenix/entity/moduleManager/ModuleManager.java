package com.shangdao.phoenix.entity.moduleManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.productionManager.ProductionManager;
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
@Table(name = "module_manager")
public class ModuleManager implements ILogEntity<ModuleManagerLog, ModuleManagerFile, ModuleManagerNotice> {
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
    private Set<ModuleManagerLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<ModuleManagerFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<ModuleManagerNotice> notices;

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

    @ManyToMany(mappedBy = "moduleManagers")
    private Set<ProductionManager> productionManagers;

    @Column(name = "display_order")
    private Integer dispalyOrder;

    @Column(name = "strategy_name")
    private String strategyName;

    @Column(name = "spare_strategy_name")
    private String spareStrategyName;

    @Column(name = "code")
    private String code;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    public ModuleManager() {

    }

    public ModuleManager(Integer dispalyOrder, String name, double price, String spareStrategyName, String code) {
        this.dispalyOrder = dispalyOrder;
        this.name = name;
        this.price = price;
        this.spareStrategyName = spareStrategyName;
        this.code = code;
        this.enabled = true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<ProductionManager> getProductionManagers() {
        return productionManagers;
    }

    public void setProductionManagers(Set<ProductionManager> productionManagers) {
        this.productionManagers = productionManagers;
    }

    public Integer getDispalyOrder() {
        return dispalyOrder;
    }

    public void setDispalyOrder(Integer dispalyOrder) {
        this.dispalyOrder = dispalyOrder;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getSpareStrategyName() {
        return spareStrategyName;
    }

    public void setSpareStrategyName(String spareStrategyName) {
        this.spareStrategyName = spareStrategyName;
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
    public Set<ModuleManagerLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<ModuleManagerLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<ModuleManagerFile> getFiles() {
        return files;
    }

    public void setFiles(Set<ModuleManagerFile> files) {
        this.files = files;
    }

    @Override
    public Set<ModuleManagerNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<ModuleManagerNotice> notices) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
