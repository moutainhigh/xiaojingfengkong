package com.shangdao.phoenix.entity.act;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.noticeTemplate.NoticeTemplate;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="act")
public class Act implements IBaseEntity{
	
	@Transient
	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="created_at")
	@JsonIgnore
	private Date createdAt;
	
	@Column(name="name")
	private String name;
	
	@Column(name="deleted_at")
	@JsonIgnore
	private Date deletedAt;
	
	@Column(name="code")
	@NotNull
	private String code;
	
	@Column(name="sort_number")
	private Integer sortNumber;
	
	@Column(name="list_visable")
	private Boolean listVisable;
	
	@Column(name="detail_visable")
	private Boolean detailVisable;

	@Column(name="icon_cls")
	private String iconCls;

	@Column(name="description")
	private String description;
	
	
	@ManyToOne
	@JoinColumn(name="target_state_id")
	private State targetState;
	
	@ManyToOne
	@JoinColumn(name="entity_manager_id")
	@NotNull
	private EntityManager entityManager;
	
	@ManyToMany
	@JoinTable(name="state_act",joinColumns = { @JoinColumn(name = "act_id") }, inverseJoinColumns = { @JoinColumn(name = "state_id") })
	private Set<State> states;
	
	@ManyToMany
    @JoinTable(name="act_role",joinColumns = { @JoinColumn(name = "act_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles;
	
	@Column(name="act_group")
	private String actGroup;
	
	@Column(name="all_can")
	private Boolean allCan;
	@Column(name="creator_can")
	private Boolean creatorCan;
	@Column(name="manager_can")
	private Boolean managerCan;
	@Column(name="member_can")
	private Boolean memberCan;
	@Column(name="department_can")
	private Boolean departmentCan;
	@Column(name="subscriber_can")
	private Boolean subscriberCan;

	@ManyToOne
	@JoinColumn(name="created_by")
	@JsonIgnore
	private User createdBy;

	@ManyToOne
	@JoinColumn(name="state_id")
	private State state;
	
	@OneToMany(mappedBy="act")
	private Set<ActNotice> actNotices;
	
	@Column(name="cancel_other_notice")
	private Boolean cancelOtherNotice;
	
	public Act(){
	    
	}
	
	public
	Act(EntityManager entityManager,String name ,String code){
	    this.creatorCan = false;
	    this.managerCan = false;
	    this.memberCan = false;
	    this.departmentCan = false;
	    this.subscriberCan = false;
	    this.listVisable = false;
	    this.detailVisable = false;
	    this.sortNumber = 0;
	    this.cancelOtherNotice = false;
	    this.allCan = true;
	    this.createdAt = new Date();
	    this.entityManager = entityManager;
	    this.name = name;
	    this.code = code;
	    this.iconCls = "icon-" + this.code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}

	public Boolean getListVisable() {
		return listVisable;
	}

	public void setListVisable(Boolean listVisable) {
		this.listVisable = listVisable;
	}

	public Boolean getDetailVisable() {
		return detailVisable;
	}

	public void setDetailVisable(Boolean detailVisable) {
		this.detailVisable = detailVisable;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public State getTargetState() {
		return targetState;
	}

	public void setTargetState(State targetState) {
		this.targetState = targetState;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Set<State> getStates() {
		return states;
	}

	public void setStates(Set<State> states) {
		this.states = states;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getActGroup() {
		return actGroup;
	}

	public void setActGroup(String actGroup) {
		this.actGroup = actGroup;
	}

	public Boolean getAllCan() {
		return allCan;
	}

	public void setAllCan(Boolean allCan) {
		this.allCan = allCan;
	}

	public Boolean getCreatorCan() {
		return creatorCan;
	}

	public void setCreatorCan(Boolean creatorCan) {
		this.creatorCan = creatorCan;
	}

	public Boolean getManagerCan() {
		return managerCan;
	}

	public void setManagerCan(Boolean managerCan) {
		this.managerCan = managerCan;
	}

	public Boolean getMemberCan() {
		return memberCan;
	}

	public void setMemberCan(Boolean memberCan) {
		this.memberCan = memberCan;
	}

	public Boolean getDepartmentCan() {
		return departmentCan;
	}

	public void setDepartmentCan(Boolean departmentCan) {
		this.departmentCan = departmentCan;
	}

	public Boolean getSubscriberCan() {
		return subscriberCan;
	}

	public void setSubscriberCan(Boolean subscriberCan) {
		this.subscriberCan = subscriberCan;
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

	public Set<ActNotice> getActNotices() {
		return actNotices;
	}

	public void setActNotices(Set<ActNotice> actNotices) {
		this.actNotices = actNotices;
	}

	public Boolean getCancelOtherNotice() {
		return cancelOtherNotice;
	}

	public void setCancelOtherNotice(Boolean cancelOtherNotice) {
		this.cancelOtherNotice = cancelOtherNotice;
	}
}
