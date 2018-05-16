package com.shangdao.phoenix.entity.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.util.TreeBuilder.TreeNode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="department")
public class Department implements TreeNode<Department>,IBaseEntity,Serializable{
	
	/**
	 * 
	 */
	@Transient
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@ManyToOne
	@JoinColumn(name="entity_manager_id")
	private EntityManager entityManager;
	
	@Column(name="name")
    private String name;

	@ManyToMany(mappedBy="departments")
	private Set<User> employees;
	
	@ManyToOne
	@JoinColumn(name="parent_id")
	private Department parent;
	
	@OneToMany(mappedBy="parent")
	private Set<Department> children;
	
	@Column(name="created_at")
	@JsonIgnore
	private Date createdAt;
	
	@Column(name="deleted_at")
	@JsonIgnore
	private Date deletedAt;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	@JsonIgnore
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name="state_id")
	private State state;
	
	@Column(name = "note")
	private String note;
	
	@Column(name="sort_number")
	private Long sortNumber;
	
	@Column(name="work_weixin_id")
	private Long workWeixinId;


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

	public Set<User> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<User> employees) {
		this.employees = employees;
	}

	@Override
	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Long sortNumber) {
		this.sortNumber = sortNumber;
	}

	public Long getWorkWeixinId() {
		return workWeixinId;
	}

	public void setWorkWeixinId(Long workWeixinId) {
		this.workWeixinId = workWeixinId;
	}
}