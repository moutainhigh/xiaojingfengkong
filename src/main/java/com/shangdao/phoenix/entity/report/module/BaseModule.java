package com.shangdao.phoenix.entity.report.module;

import com.shangdao.phoenix.enums.Color;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * @author huanghengkun
 * @date 2018/04/02
 */
@MappedSuperclass
public abstract class BaseModule {

    @Column(name = "displaySort")
    protected Integer displaySort;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    protected Color color;

    public BaseModule() {
    }

    public BaseModule(Color color) {
        this.color = color;
    }

    public Integer getDisplaySort() {
        return displaySort;
    }

    public void setDisplaySort(Integer displaySort) {
        this.displaySort = displaySort;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (isRiskHigher(color)) {
            this.color = color;
        }
    }

    private boolean isRiskHigher(Color newColor) {
        return this.color == null || this.color.compareTo(newColor) < 0;
    }
}
