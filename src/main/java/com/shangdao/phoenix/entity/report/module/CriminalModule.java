package com.shangdao.phoenix.entity.report.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * @author huanghengkun
 * @date 2018/04/09
 */
@Entity
@Table(name = "module_criminal")
public class CriminalModule extends BaseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 具体刑事犯罪
     */
    @Column(name = "criminals")
    private String criminals;

    /**
     * 刑事犯罪数量
     */
    @Column(name = "count")
    private Integer count;

    public CriminalModule() {
    }

    public CriminalModule(Color color) {
        super(color);
    }

    @Override
    public String toString() {
        return "CriminalModule{" +
                "id=" + id +
                ", criminals='" + criminals + '\'' +
                ", count=" + count +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONArray getCriminals() {
        return JSON.parseArray(criminals);
    }

    public void setCriminals(JSONArray criminals) {
        this.criminals = criminals.toJSONString();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
