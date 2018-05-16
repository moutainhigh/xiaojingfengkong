package com.shangdao.phoenix.entity.companybill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.act.ActNotice;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.INoticeLog;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "company_bill_notice")
public class CompanyBillNotice implements INoticeLog<CompanyBill, CompanyBillLog> {
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
    private CompanyBill entity;

    @ManyToOne
    @JoinColumn(name = "act_id")
    private Act act;

    @ManyToOne
    @JoinColumn(name="log_id")
    private CompanyBillLog log;

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
    private ActNotice.DelayType delayType;

    @Column(name="notice_channel")
    @Enumerated(EnumType.STRING)
    private NoticeTemplate.NoticeChannel noticeChannel;

    @Column(name="role_code")
    private String roleCode;

    @ManyToOne
    @JoinColumn(name="notice_template_id")
    private NoticeTemplate noticeTemplate;

    @Column(name="success")
    private boolean success;

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
    public CompanyBill getEntity() {
        return entity;
    }

    @Override
    public void setEntity(CompanyBill entity) {
        this.entity = entity;
    }

    @Override
    public Act getAct() {
        return act;
    }

    @Override
    public void setAct(Act act) {
        this.act = act;
    }

    @Override
    public CompanyBillLog getLog() {
        return log;
    }

    @Override
    public void setLog(CompanyBillLog log) {
        this.log = log;
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

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public User getToUser() {
        return toUser;
    }

    @Override
    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @Override
    public ActNotice.DelayType getDelayType() {
        return delayType;
    }

    @Override
    public void setDelayType(ActNotice.DelayType delayType) {
        this.delayType = delayType;
    }

    @Override
    public NoticeTemplate.NoticeChannel getNoticeChannel() {
        return noticeChannel;
    }

    @Override
    public void setNoticeChannel(NoticeTemplate.NoticeChannel noticeChannel) {
        this.noticeChannel = noticeChannel;
    }

    @Override
    public String getRoleCode() {
        return roleCode;
    }

    @Override
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public NoticeTemplate getNoticeTemplate() {
        return noticeTemplate;
    }

    @Override
    public void setNoticeTemplate(NoticeTemplate noticeTemplate) {
        this.noticeTemplate = noticeTemplate;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }
}