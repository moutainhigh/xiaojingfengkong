package com.shangdao.phoenix.entity.state;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
	State findByEntityManagerIdAndCode(long id,String code);
	
}
