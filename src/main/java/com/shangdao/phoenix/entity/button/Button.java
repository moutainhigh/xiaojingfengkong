package com.shangdao.phoenix.entity.button;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.menu.Menu;
import com.shangdao.phoenix.entity.role.Role;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;

@Entity
@Table(name = "button")
public class Button implements IBaseEntity{
    /**
     * 
     */
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
    
    @ManyToOne
    @JoinColumn(name="created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name="state_id")
    private State state;
    
    @ManyToOne
    @JoinColumn(name="entity_manager_id")
    private EntityManager entityManager;
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    
    @ManyToMany(mappedBy = "buttons")
    private Set<Role> roles;
    
    @Column(name = "code")
    private String code;

    public Button(){

    }

    public Button(String name,String code,Menu menu){
        this.name = name;
        this.code = code;
        this.menu = menu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    

}
