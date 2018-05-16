package com.shangdao.phoenix.entity.repairorder;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;

@Entity
@Table(name = "repair_order")
public class RepairOrder implements ILogEntity<RepairOrderLog, RepairOrderFile, RepairOrderNotice>{
    
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
    private Set<RepairOrderLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<RepairOrderFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<RepairOrderNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

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

    public List<OssImage> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<OssImage> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<RepairOrderLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<RepairOrderLog> logs) {
        this.logs = logs;
    }

    public Set<RepairOrderFile> getFiles() {
        return files;
    }

    public void setFiles(Set<RepairOrderFile> files) {
        this.files = files;
    }

    public Set<RepairOrderNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<RepairOrderNotice> notices) {
        this.notices = notices;
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

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
    

}
