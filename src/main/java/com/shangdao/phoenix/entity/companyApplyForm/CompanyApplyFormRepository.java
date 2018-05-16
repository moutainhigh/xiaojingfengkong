package com.shangdao.phoenix.entity.companyApplyForm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shangdao.phoenix.entity.state.State;

/**
 * @author duyiting
 * @date 2018/03/30
 */
public interface CompanyApplyFormRepository extends JpaRepository<CompanyApplyForm, Long> {
    
    List<CompanyApplyForm> findByCreatedById(long id);
    
    List<CompanyApplyForm> findByName(String name);
    
    long countByNameAndState(String name,State state);
    
    long countByCreatedByIdAndState(long id,State state);
}
