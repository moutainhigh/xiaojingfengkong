package com.shangdao.phoenix.entity.report.module;

import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * alipay芝麻积分Module
 *
 * @author duyiting
 * @date 2018/04/10
 */
@Entity
@Table(name = "module_zhima_score")
public class ZhimaScoreModule extends BaseModule{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 芝麻分
     */
    private String score;

    public ZhimaScoreModule() {
    }

    public ZhimaScoreModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
