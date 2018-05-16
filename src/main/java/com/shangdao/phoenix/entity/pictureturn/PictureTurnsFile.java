package com.shangdao.phoenix.entity.pictureturn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.phoenix.entity.act.Act;
import com.shangdao.phoenix.entity.entityManager.EntityManager;
import com.shangdao.phoenix.entity.interfaces.IFile;
import com.shangdao.phoenix.entity.state.State;
import com.shangdao.phoenix.entity.user.User;
import com.shangdao.phoenix.util.FileFormat;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "picture_turns_file")
public class PictureTurnsFile implements IFile<PictureTurns,PictureTurnsLog> {

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
    private PictureTurns entity;

    @ManyToOne
    @JoinColumn(name = "act_id")
    private Act act;

    @ManyToOne
    @JoinColumn(name = "log_id")
    private PictureTurnsLog log;

    @Column(name = "create_at")
    private Date createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "new_file_name")
    private String newFileName;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "file_format")
    @Enumerated(EnumType.STRING)
    private FileFormat fileFormat;

    @Column(name = "url")
    private String url;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "small_image")
    private String smallImage;

    @Column(name = "middle_image")
    private String middleImage;

    @Column(name = "large_image")
    private String largeImage;

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
    public PictureTurns getEntity() {
        return entity;
    }

    @Override
    public void setEntity(PictureTurns entity) {
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
    public PictureTurnsLog getLog() {
        return log;
    }

    @Override
    public void setLog(PictureTurnsLog log) {
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
    public String getOriginalFileName() {
        return originalFileName;
    }

    @Override
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    @Override
    public String getNewFileName() {
        return newFileName;
    }

    @Override
    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public FileFormat getFileFormat() {
        return fileFormat;
    }

    @Override
    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String getSmallImage() {
        return smallImage;
    }

    @Override
    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    @Override
    public String getMiddleImage() {
        return middleImage;
    }

    @Override
    public void setMiddleImage(String middleImage) {
        this.middleImage = middleImage;
    }

    @Override
    public String getLargeImage() {
        return largeImage;
    }

    @Override
    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
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