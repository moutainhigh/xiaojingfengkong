package com.shangdao.phoenix.entity.act;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

@Entity
@Table(name = "act_notice")
public class ActNotice implements IBaseEntity {

	@Transient
	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "created_at")
	@JsonIgnore
	private Date createdAt;

	@Column(name = "name")
	private String name;

	@Column(name = "deleted_at")
	@JsonIgnore
	private Date deletedAt;

	@ManyToOne
	@JoinColumn(name="entity_manager_id")
	private EntityManager entityManager;
	
	@ManyToOne
	@JoinColumn(name="state_id")
	private State state;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	@JsonIgnore
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name="act_id")
	private Act act;

	@Column(name="delay_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private DelayType delayType; 
	
	@Column(name="delay_time")
	private Integer delayTime;
	
	@Column(name="can_be_cancelled")
	private Boolean canBeCancelled;
	
	@Column(name="role_code")
	@NotBlank
	private String roleCode;
	
	@ManyToOne
	@JoinColumn(name="notice_template")
	@NotNull
	private NoticeTemplate noticeTemplate;

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
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
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
	public User getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Act getAct() {
		return act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

	public DelayType getDelayType() {
		return delayType;
	}

	public void setDelayType(DelayType delayType) {
		this.delayType = delayType;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public Boolean getCanBeCancelled() {
		return canBeCancelled;
	}

	public void setCanBeCancelled(Boolean canBeCancelled) {
		this.canBeCancelled = canBeCancelled;
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

	public enum DelayType{
		IMMEDIATELY,DELAY
	}
}
