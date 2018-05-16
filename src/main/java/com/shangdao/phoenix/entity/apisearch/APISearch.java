package com.shangdao.phoenix.entity.apisearch;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "api_search", indexes = {@Index(name = "idx_api_search_code_parameters", columnList = "code,parameters")})
public class APISearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "result")
    @Lob
    private String result;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "code")
    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
