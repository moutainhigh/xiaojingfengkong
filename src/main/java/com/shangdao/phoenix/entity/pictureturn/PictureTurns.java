package com.shangdao.phoenix.entity.pictureturn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILogEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.service.FileUploadService.OssImage;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "picture_turns")
public class PictureTurns implements ILogEntity<PictureTurnsLog, PictureTurnsFile, PictureTurnsNotice> {
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
    private Set<PictureTurnsLog> logs;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<PictureTurnsFile> files;

    @OneToMany(mappedBy = "entity")
    @JsonIgnore
    private Set<PictureTurnsNotice> notices;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @Column(name = "last_modified_at")
    private Date lastModifiedAt;
    
    @Column(name = "sort")
    private Integer sort;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "turn_type")
    private PictureTurnType turnType;
    
    @Column(name = "click_times")
    private Long clickTimes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "turn_status")
    private PictureTurnStatus turnStatus;
    
    @Column(name = "jump_url")
    private String  jumpUrl;
    
    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public PictureTurnStatus getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(PictureTurnStatus turnStatus) {
        this.turnStatus = turnStatus;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public PictureTurnType getTurnType() {
        return turnType;
    }

    public void setTurnType(PictureTurnType turnType) {
        this.turnType = turnType;
    }

    public Long getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Long clickTimes) {
        this.clickTimes = clickTimes;
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
    public Set<PictureTurnsLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<PictureTurnsLog> logs) {
        this.logs = logs;
    }

    @Override
    public Set<PictureTurnsFile> getFiles() {
        return files;
    }

    public void setFiles(Set<PictureTurnsFile> files) {
        this.files = files;
    }

    @Override
    public Set<PictureTurnsNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<PictureTurnsNotice> notices) {
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
    
    public enum  PictureTurnType{
        WEB,
        APP;
    }
    
    public enum PictureTurnStatus{
        
        SHELVES, //上架中
        THESHELVES  //下架中
    }
    
}