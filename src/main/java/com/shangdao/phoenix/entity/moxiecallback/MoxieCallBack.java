package com.shangdao.phoenix.entity.moxiecallback;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "moxie_call_back")
public class MoxieCallBack {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @Column(name = "task_id")
    private String taskId;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "event")
    private String event;
    
    @Column(name = "header",columnDefinition = "text")
    private String header;
    
    @Column(name = "body",columnDefinition = "text")
    private String body;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MoxieTaskStatusEnum status;
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MoxieTaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MoxieTaskStatusEnum status) {
        this.status = status;
    }



    public enum MoxieTaskStatusEnum {
        /**
         * 已创建    step 1
         */
        CREATED,
        /**
         * 授权登录失败    step 2.1
         */
        LOGIN_FAIL,
        /**
         * 已授权登录    step 2.2
         */
        LOGIN_SUCCESS,
        /**
         * 采集失败    step 3.1
         */
        FETCH_FAIL,
        /**
         * 采集成功    step 3.2
         */
        FETCH_SUCCESS,
        /**
         * 报告生成失败    step 4.1
         */
        REPORT_FAIL,
        /**
         * 报告生成成功    step 4.2
         */
        REPORT_SUCCESS
    }
    

}
