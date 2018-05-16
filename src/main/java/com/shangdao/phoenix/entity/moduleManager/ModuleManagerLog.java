package com.shangdao.phoenix.entity.moduleManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILog;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.util.HTTPHeader.Terminal;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author huanghengkun
 * @date 2018/04/02
 */
@Entity
@Table(name = "module_manager_log")
public class ModuleManagerLog implements ILog<ModuleManager, ModuleManagerFile, ModuleManagerNotice> {
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
    @JoinColumn(name = "entity_manager_id")
    private EntityManager entityManager;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private ModuleManager entity;

    @Column(name = "create_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "act_id")
    private Act act;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "before_state_id")
    @ManyToOne
    private State beforeState;

    @JoinColumn(name = "after_state_id")
    @ManyToOne
    private State afterState;

    @Column(name = "difference")
    @Lob
    private String difference;

    @Column(name = "ip")
    private String ip;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "imei")
    private String imei;

    @Column(name = "terminal")
    @Enumerated(EnumType.STRING)
    private Terminal terminal;

    @OneToMany(mappedBy = "log")
    private Set<ModuleManagerFile> files;

    @OneToMany(mappedBy = "log")
    private Set<ModuleManagerNotice> notices;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

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
    public ModuleManager getEntity() {
        return entity;
    }

    @Override
    public void setEntity(ModuleManager entity) {
        this.entity = entity;
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
    public Act getAct() {
        return act;
    }

    @Override
    public void setAct(Act act) {
        this.act = act;
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
    public State getBeforeState() {
        return beforeState;
    }

    @Override
    public void setBeforeState(State beforeState) {
        this.beforeState = beforeState;
    }

    @Override
    public State getAfterState() {
        return afterState;
    }

    @Override
    public void setAfterState(State afterState) {
        this.afterState = afterState;
    }

    @Override
    public String getDifference() {
        return difference;
    }

    @Override
    public void setDifference(String difference) {
        this.difference = difference;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String getImei() {
        return imei;
    }

    @Override
    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public Terminal getTerminal() {
        return terminal;
    }

    @Override
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public Set<ModuleManagerFile> getFiles() {
        return files;
    }

    @Override
    public void setFiles(Set<ModuleManagerFile> files) {
        this.files = files;
    }

    @Override
    public Set<ModuleManagerNotice> getNotices() {
        return notices;
    }

    @Override
    public void setNotices(Set<ModuleManagerNotice> notices) {
        this.notices = notices;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
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
}
