package com.shangdao.phoenix.entity.report.module;

import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * 基础信息模块
 *
 * @author huanghengkun
 * @date 2018/04/04
 */
@Entity
@Table(name = "module_basic_info")
public class BasicInfoModule extends BaseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;
    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;
    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;
    /**
     * 出身地省
     */
    @Column(name = "province")
    private String province;
    /**
     * 出身地市
     */
    @Column(name = "city")
    private String city;
    /**
     * 出身地县区
     */
    @Column(name = "town")
    private String town;
    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;
    /**
     * 运营商
     */
    @Column(name = "mobile_company")
    private String mobileCompany;
    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 银行卡号
     */
    @Column(name = "bank_card")
    private String bankCard;
    /**
     * 银行
     */
    @Column(name = "bank_name")
    private String bankName;
    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    public BasicInfoModule() {
    }

    public BasicInfoModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobileCompany() {
        return mobileCompany;
    }

    public void setMobileCompany(String mobileCompany) {
        this.mobileCompany = mobileCompany;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
