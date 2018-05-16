package com.shangdao.phoenix.entity.report.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * 多头借贷模块
 *
 * @author huanghengkun
 * @date 2018/04/03
 */
@Entity
@Table(name = "module_multiple_head_lend")
public class MultipleHeadLendModule extends BaseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "black_risks", columnDefinition = "text")
    private String blackRisks;

    public MultipleHeadLendModule() {
    }

    public MultipleHeadLendModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONArray getBlackRisks() {
        return JSON.parseArray(blackRisks);
    }

    public void setBlackRisks(JSONArray blackRisks) {
        this.blackRisks = blackRisks.toJSONString();
    }

}
