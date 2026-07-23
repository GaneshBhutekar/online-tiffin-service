package com.cdac.onlineTiffinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.MenuItem;

public interface MenuRepository extends JpaRepository<MenuItem, Long>{

}
