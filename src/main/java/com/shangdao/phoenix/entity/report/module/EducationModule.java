package com.shangdao.phoenix.entity.report.module;

import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * 学历信息
 *
 * @author huanghengkun
 * @date 2018/02/26
 */
@Entity
@Table(name = "module_education")
public class EducationModule extends BaseModule {

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
     * 毕业结论
     */
    @Column(name = "study_result")
    private String studyResult;
    /**
     * 毕业时间
     */
    @Column(name = "graduate_time")
    private String graduateTime;
    /**
     * 学校
     */
    @Column(name = "college")
    private String college;
    /**
     * 学历
     */
    @Column(name = "degree")
    private String degree;
    /**
     * 院校类型
     */
    @Column(name = "college_type")
    private String collegeType;

    public EducationModule() {
    }

    public EducationModule(Color color) {
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

    public String getStudyResult() {
        return studyResult;
    }

    public void setStudyResult(String studyResult) {
        this.studyResult = studyResult;
    }

    public String getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(String graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCollegeType() {
        return collegeType;
    }

    public void setCollegeType(String collegeType) {
        this.collegeType = collegeType;
    }

}
