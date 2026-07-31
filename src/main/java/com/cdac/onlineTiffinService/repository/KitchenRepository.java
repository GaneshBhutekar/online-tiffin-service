package com.cdac.onlineTiffinService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.Kitchen;

public interface KitchenRepository extends JpaRepository<Kitchen, Long>{
	
	Page<Kitchen> findBykitchenCity(String city,Pageable pageable);
	
//	List<Kitchen> findByBlockedFalse();
	//pagination required
	
	Page<Kitchen> findByBlockedFalse(Pageable pageable);
	
	List<Kitchen> findByBlockedTrue();
	
}
