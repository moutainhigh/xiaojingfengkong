package com.shangdao.phoenix.entity.report.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.shangdao.phoenix.enums.Color;

import javax.persistence.*;

/**
 * ali失信黑名单
 * @author duyiting
 * @date 2018/04/08
 */
@Entity
@Table(name = "module_dishonest_black")
public class DishonestBlackModule extends BaseModule{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     *具体失信记录
     */
    @Column(name = "dishonest_blacks",columnDefinition = "text")
    private String dishonestBlacks;

    /**
     * 失信数量
     */
    private Integer count;

    public DishonestBlackModule() {
    }

    public DishonestBlackModule(Color color) {
        super(color);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONArray getDishonestBlacks() {
        return JSON.parseArray(dishonestBlacks);
    }

    public void setDishonestBlacks(JSONArray dishonestBlacks) {
        this.dishonestBlacks = dishonestBlacks.toJSONString();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
