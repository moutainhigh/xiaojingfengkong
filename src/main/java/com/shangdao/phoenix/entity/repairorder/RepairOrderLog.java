package com.shangdao.phoenix.entity.repairorder;

import java.util.Date;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.ILog;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.util.HTTPHeader.Terminal;

@Entity
@Table(name = "repair_order_log")
public class RepairOrderLog implements ILog<RepairOrder, RepairOrderFile, RepairOrderNotice>{
    
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
    private RepairOrder entity;

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
    private Set<RepairOrderFile> files;

    @OneToMany(mappedBy = "log")
    private Set<RepairOrderNotice> notices;

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


    public RepairOrder getEntity() {
        return entity;
    }

    public void setEntity(RepairOrder entity) {
        this.entity = entity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(State beforeState) {
        this.beforeState = beforeState;
    }

    public State getAfterState() {
        return afterState;
    }

    public void setAfterState(State afterState) {
        this.afterState = afterState;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Set<RepairOrderFile> getFiles() {
        return files;
    }

    public void setFiles(Set<RepairOrderFile> files) {
        this.files = files;
    }

    public Set<RepairOrderNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<RepairOrderNotice> notices) {
        this.notices = notices;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
    
    

}
