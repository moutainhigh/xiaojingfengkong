package com.shangdao.phoenix.entity.menu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    Menu findByName(String name);
}
