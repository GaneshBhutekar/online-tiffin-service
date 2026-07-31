package com.cdac.onlineTiffinService.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.MenuItem;

public interface MenuRepository extends JpaRepository<MenuItem, Long>{
//	List<MenuItem> findByKitchenId(Long kitchenId); // menuitem.kitchen.id
	Page<MenuItem> findByKitchenId(Long kitchenId,Pageable pageable);
	
	List<MenuItem> findByKitchenIdAndAvailableTrue(Long kitchenId);
	
	List<MenuItem> findByDishNameContainingIgnoreCase(String keyword);
	List<MenuItem> findByDishNameContainingIgnoreCaseAndAvailableTrue(String keyword);
	List<MenuItem> findByPriceBetweenAndAvailableTrue(
	        BigDecimal minPrice,
	        BigDecimal maxPrice);
	
	Optional<MenuItem> findByIdAndAvailableTrue(Long id);
}
