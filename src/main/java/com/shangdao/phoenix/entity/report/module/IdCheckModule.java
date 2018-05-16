package com.shangdao.phoenix.entity.report.module;

import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * 反欺诈
 * @author duyiting
 * @date 2018/04/12
 */
@Entity
@Table(name = "module_id_check")
public class IdCheckModule extends BaseModule{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 姓名手机号身份证三要素
     */
    private String nmPhCn;
    /**
     * 地址与身份证
     */
    private String adCn;
    /**
     * 银行卡与身份证
     */
    private String bcCn;

    public IdCheckModule() {
    }

    public IdCheckModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNmPhCn() {
        return nmPhCn;
    }

    public void setNmPhCn(String nmPhCn) {
        this.nmPhCn = nmPhCn;
    }

    public String getAdCn() {
        return adCn;
    }

    public void setAdCn(String adCn) {
        this.adCn = adCn;
    }

    public String getBcCn() {
        return bcCn;
    }

    public void setBcCn(String bcCn) {
        this.bcCn = bcCn;
    }
}
