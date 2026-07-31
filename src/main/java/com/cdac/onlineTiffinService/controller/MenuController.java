package com.cdac.onlineTiffinService.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.onlineTiffinService.dto.MenuAvailabilityDto;
import com.cdac.onlineTiffinService.dto.MenuRequestDto;
import com.cdac.onlineTiffinService.dto.MenuResponseDto;
import com.cdac.onlineTiffinService.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;
	
	
	
	@PostMapping("/kitchen/{kitchenId}")
	public ResponseEntity<MenuResponseDto> addMenuItem(
	        @PathVariable Long kitchenId,
	        @Valid @RequestBody MenuRequestDto dto) {

	    MenuResponseDto response =
	            menuService.addMenuItem(kitchenId, dto);

	    return new ResponseEntity<>(response, HttpStatus.CREATED); //201
	}
	
	@GetMapping("/kitchen/{kitchenId}")
	public ResponseEntity<Page<MenuResponseDto>>
	getMenuItemsByKitchen(
	        @PathVariable Long kitchenId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	        
			
			) {

	    Page<MenuResponseDto> response =
	            menuService.getMenuItemsByKitchen(kitchenId,page,size);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MenuResponseDto> getMenuItemById(
	        @PathVariable Long id) {

	    MenuResponseDto response =
	            menuService.getMenuItemById(id);

	    return ResponseEntity.ok(response);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<MenuResponseDto> updateMenuItem(
	        @PathVariable Long id,
	        @Valid @RequestBody MenuRequestDto requestDto) {

	    MenuResponseDto response =
	            menuService.updateMenu(id, requestDto);

	    return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteMenuItem(
	        @PathVariable Long id) {

	    menuService.deleteMenuItem(id);

	    return ResponseEntity.ok("Menu Item deleted successfully.");
	}
	
	
	
	/*
	 @GetMapping("/kitchen/{kitchenId}/available")
	public ResponseEntity<List<MenuResponseDto>>
	getAvailableMenuItems(
	        @PathVariable Long kitchenId) {

	    List<MenuResponseDto> response =
	            menuService.getAvailableMenuItems(kitchenId);

	    return ResponseEntity.ok(response);
	} 
	 */
	@GetMapping("/kitchen/{kitchenId}/available")
	public ResponseEntity<List<MenuResponseDto>>
	getAvailableMenuItems(
	        @PathVariable Long kitchenId) {

	    List<MenuResponseDto> response =
	            menuService.getAvailableMenuItems(kitchenId);

	    return ResponseEntity.ok(response);
	}
	
	@PatchMapping("/{id}/availability")
	public ResponseEntity<MenuResponseDto> changeAvailability(
	        @PathVariable Long id,
	        @RequestBody MenuAvailabilityDto dto) {

	    MenuResponseDto response =
	            menuService.changeAvailability(id, dto);

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/search")
	public ResponseEntity<List<MenuResponseDto>> searchDish(
	        @RequestParam String keyword) {

	    List<MenuResponseDto> response =
	            menuService.searchDish(keyword);

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/customer/search")
	public ResponseEntity<List<MenuResponseDto>> searchAvailableDish(
	        @RequestParam String keyword) {

	    List<MenuResponseDto> response =
	            menuService.searchAvailableDish(keyword);

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/filter")
	public ResponseEntity<List<MenuResponseDto>> filterByPrice(
	        @RequestParam BigDecimal minPrice,
	        @RequestParam BigDecimal maxPrice) {

	    List<MenuResponseDto> response =
	            menuService.filterByPrice(minPrice, maxPrice);

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/admin")
	public ResponseEntity<List<MenuResponseDto>>
	getAllMenuItems() {

	    List<MenuResponseDto> response =
	            menuService.getAllMenuItems();

	    return ResponseEntity.ok(response);
	}	
}
