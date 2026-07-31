package com.cdac.onlineTiffinService.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cdac.onlineTiffinService.dto.MenuAvailabilityDto;
import com.cdac.onlineTiffinService.dto.MenuRequestDto;
import com.cdac.onlineTiffinService.dto.MenuResponseDto;
import com.cdac.onlineTiffinService.model.FoodCategory;

public interface MenuService {
	MenuResponseDto addMenuItem(Long kitchenId,MenuRequestDto dto);
//	List<MenuResponseDto> getMenuItemsByKitchen(Long kitchenId);
	Page<MenuResponseDto> getMenuItemsByKitchen(

			Long kitchenId,

			int page,

			int size,

			FoodCategory category,

			Boolean available,

			BigDecimal minPrice,

			BigDecimal maxPrice,

			String sortBy,

			String direction

			);
	
	MenuResponseDto getMenuItemById(Long id);
	
	MenuResponseDto updateMenu(Long id , MenuRequestDto requestDto);
	
	void deleteMenuItem(Long id);
	
	List<MenuResponseDto> getAvailableMenuItems(Long kitchenId);
	MenuResponseDto changeAvailability(
	        Long id,
	        MenuAvailabilityDto dto);
	List<MenuResponseDto> searchDish(String keyword);
	
	List<MenuResponseDto> searchAvailableDish(String keyword);
	
	List<MenuResponseDto> filterByPrice(
	        BigDecimal minPrice,
	        BigDecimal maxPrice);
	List<MenuResponseDto> getAllMenuItems();
}


