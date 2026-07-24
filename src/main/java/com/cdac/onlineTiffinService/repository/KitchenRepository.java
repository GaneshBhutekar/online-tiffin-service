package com.cdac.onlineTiffinService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.Kitchen;

public interface KitchenRepository extends JpaRepository<Kitchen, Long>{
	
	List<Kitchen> findBykitchenCity(String city);
	
	List<Kitchen> findByBlockedFalse();
	List<Kitchen> findByBlockedTrue();
	
}
