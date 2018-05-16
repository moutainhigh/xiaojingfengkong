package com.shangdao.phoenix.entity.apisearch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface APISearchRepository extends JpaRepository<APISearch, Long> {
    APISearch findFirstByCodeAndParametersOrderByCreatedAtDesc(String code, String parameters);
}
