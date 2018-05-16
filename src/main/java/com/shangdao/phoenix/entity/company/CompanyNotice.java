package com.shangdao.phoenix.entity.company;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActNotice.DelayType;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.INoticeLog;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate.NoticeChannel;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

@Entity
@Table(name = "company_notice")
public class CompanyNotice implements INoticeLog<Company, CompanyLog>{
    
    /**
     * 
     */
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @ManyToOne
    @JoinColumn(name="entity_manager_id")
    private EntityManager entityManager;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private Company entity;
    
    @ManyToOne
    @JoinColumn(name = "act_id")
    private Act act;
    
    @ManyToOne
    @JoinColumn(name="log_id")
    private CompanyLog log;

    @Column(name = "create_at")
    private Date createdAt;

    @Column(name = "name")
    private String name;
    
    @Column(name="deleted_at")
    private Date deletedAt;
    
    @ManyToOne
    @JoinColumn(name="created_by")
    @JsonIgnore
    private User createdBy;
    
    @ManyToOne
    @JoinColumn(name="state_id")
    private State state;
    
    @Column(name="content")
    @Lob
    private String content;
    
    @ManyToOne
    @JoinColumn(name="to_user_id")
    private User toUser;
    
    @Column(name="delay_type")
    @Enumerated(EnumType.STRING)
    private DelayType delayType;
    
    @Column(name="notice_channel")
    @Enumerated(EnumType.STRING)
    private NoticeChannel noticeChannel;
    
    @Column(name="role_code")
    private String roleCode;
    
    @ManyToOne
    @JoinColumn(name="notice_template_id")
    private NoticeTemplate noticeTemplate;
    
    @Column(name="success")
    private boolean success;

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

    public Company getEntity() {
        return entity;
    }

    public void setEntity(Company entity) {
        this.entity = entity;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public CompanyLog getLog() {
        return log;
    }

    public void setLog(CompanyLog log) {
        this.log = log;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public DelayType getDelayType() {
        return delayType;
    }

    public void setDelayType(DelayType delayType) {
        this.delayType = delayType;
    }

    public NoticeChannel getNoticeChannel() {
        return noticeChannel;
    }

    public void setNoticeChannel(NoticeChannel noticeChannel) {
        this.noticeChannel = noticeChannel;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public NoticeTemplate getNoticeTemplate() {
        return noticeTemplate;
    }

    public void setNoticeTemplate(NoticeTemplate noticeTemplate) {
        this.noticeTemplate = noticeTemplate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    
   
}
