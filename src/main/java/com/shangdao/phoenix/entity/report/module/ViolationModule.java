package com.shangdao.phoenix.entity.report.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * 车辆违章查询
 * @author duyiting
 * @date 2018/04/09
 */
@Entity
@Table(name = "module_violation")
public class ViolationModule extends BaseModule{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 具体违章记录
     */
    @Column(columnDefinition = "text")
    private String violations;

    /**
     * 违章数量
     */
    private Integer count;

    public ViolationModule(){
    }

    public ViolationModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONArray getViolations() {
        return JSON.parseArray(violations);
    }

    public void setViolations(JSONArray violations) {
        this.violations = JSON.toJSONString(violations);
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
