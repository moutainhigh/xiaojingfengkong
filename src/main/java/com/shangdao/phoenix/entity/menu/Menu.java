package com.shangdao.phoenix.entity.menu;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.button.Button;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.util.TreeBuilder.TreeNode;

@Entity
@Table(name="menu")
public class Menu implements TreeNode<Menu>,IBaseEntity,Serializable{
	
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
	
	@Column(name="name",unique = true)
    private String name;

	@Column(name="path")
	private String path;

	@Column(name="title")
	private String title;
	
	@Column(name="icon")
	private String icon;
	
	@ManyToOne
	@JoinColumn(name="target_entity_manager_id")
	private EntityManager targetEntityManager;

	@ManyToMany(mappedBy="menus")
	private Set<Role> roles;
	
	@ManyToOne
	@JoinColumn(name="parent_id")
		private Menu parent;
	
	@OneToMany(mappedBy="parent")
	private Set<Menu> children;
	
	@Column(name="created_at")
	@JsonIgnore
	private Date createdAt;
	
	@Column(name="sort_number")
	private Integer sortNumber;
	
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
	
	@OneToMany(mappedBy = "menu")
	private Set<Button> buttons;

	@Column(name = "developer_menu")
	private Boolean developerMenu;

	public Boolean getDeveloperMenu() {
		return developerMenu;
	}

	public void setDeveloperMenu(Boolean developerMenu) {
		this.developerMenu = developerMenu;
	}

	public Menu(){

	}

	public Menu(String name,String path,Integer sortNumber,Menu parent,String icon,boolean developerMenu){
		this.name = name;
		this.path = path;
		this.createdAt = new Date();
		this.sortNumber = sortNumber;
		this.parent = parent;
		this.icon = icon;
		this.developerMenu = developerMenu;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public EntityManager getTargetEntityManager() {
		return targetEntityManager;
	}

	public void setTargetEntityManager(EntityManager targetEntityManager) {
		this.targetEntityManager = targetEntityManager;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
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

	public Integer getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
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

	public Set<Button> getButtons() {
		return buttons;
	}

	public void setButtons(Set<Button> buttons) {
		this.buttons = buttons;
	}
}