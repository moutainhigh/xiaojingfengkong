package com.shangdao.phoenix.entity.report.module;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.shangdao.phoenix.enums.Color;

@Entity
@Table(name = "court_ruled")
public class CourtRuledMoudle extends BaseModule{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "courtRuledMoudle")
    private Set<CourtRuledDetail>  courtRuledDetails;
    
    @Column(name = "count")
    private  Integer count;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<CourtRuledDetail> getCourtRuledDetails() {
        return courtRuledDetails;
    }

    public void setCourtRuledDetails(Set<CourtRuledDetail> courtRuledDetails) {
        this.courtRuledDetails = courtRuledDetails;
    }

    public CourtRuledMoudle(){
        
    }
    
    public CourtRuledMoudle(Color color){
        super(color);
    }
}
