package com.shangdao.phoenix.entity.entityManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.interfaces.IBaseEntity;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.tag.Tag;
import com.shangdao.phoenix.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "entity_manager")
public class EntityManager implements IBaseEntity, Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "has_log")
    private Boolean hasLog;
    @Column(name = "has_project")
    private Boolean hasProject;
    @Column(name = "has_state_machine")
    private Boolean hasStateMachine;
    @Column(name = "has_tag")
    private Boolean hasTag;


    @Column(name = "deleted_at")
    @JsonIgnore
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;


    @OneToMany(mappedBy = "entityManager")
    private Set<State> states;
    @OneToMany(mappedBy = "entityManager")
    private Set<Act> acts;
    @OneToMany(mappedBy = "entityManager")
    private Set<Tag> tags;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "manager_group")
    @Enumerated(EnumType.STRING)
    private ManagerGroup managerGroup;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasLog() {
        return hasLog;
    }

    public void setHasLog(Boolean hasLog) {
        this.hasLog = hasLog;
    }

    public Boolean getHasProject() {
        return hasProject;
    }

    public void setHasProject(Boolean hasProject) {
        this.hasProject = hasProject;
    }

    public Boolean getHasStateMachine() {
        return hasStateMachine;
    }

    public void setHasStateMachine(Boolean hasStateMachine) {
        this.hasStateMachine = hasStateMachine;
    }

    public Boolean getHasTag() {
        return hasTag;
    }

    public void setHasTag(Boolean hasTag) {
        this.hasTag = hasTag;
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

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public Set<Act> getActs() {
        return acts;
    }

    public void setActs(Set<Act> acts) {
        this.acts = acts;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public ManagerGroup getManagerGroup() {
        return managerGroup;
    }

    public void setManagerGroup(ManagerGroup managerGroup) {
        this.managerGroup = managerGroup;
    }

    public enum ManagerGroup {
        DEVELOPER, ADMIN
    }

}
