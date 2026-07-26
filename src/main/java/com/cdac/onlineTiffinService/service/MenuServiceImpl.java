package com.cdac.onlineTiffinService.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cdac.onlineTiffinService.dto.MenuAvailabilityDto;
import com.cdac.onlineTiffinService.dto.MenuRequestDto;
import com.cdac.onlineTiffinService.dto.MenuResponseDto;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;
import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.model.MenuItem;
import com.cdac.onlineTiffinService.repository.KitchenRepository;
import com.cdac.onlineTiffinService.repository.MenuRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	
	public final MenuRepository menuRepository;
	public final KitchenRepository kitchenRepository;
	
	private final ModelMapper modelMapper;

	@Override
	public MenuResponseDto addMenuItem(Long kitchenId, MenuRequestDto dto) {
		Kitchen kitchen =
		        kitchenRepository.findById(kitchenId)
		        .orElseThrow(()->
		            new ResourceNotFoundException(
		                "Kitchen",
		                "id",
		                kitchenId));
		// converting dto to entity
		
		MenuItem menu = modelMapper.map(dto, MenuItem.class);
		
		menu.setKitchen(kitchen);
		MenuItem saved =
		        menuRepository.save(menu);
		
		MenuResponseDto response =
		        modelMapper.map(saved,
		                        MenuResponseDto.class);
		response.setKitchenId(saved.getKitchen().getId());
		response.setKitchenName(saved.getKitchen().getKitchenName());
		
		return response; 
	}
	@Override
	public List<MenuResponseDto> getMenuItemsByKitchen(Long kitchenId) {

	    // Check whether kitchen exists
	    Kitchen kitchen = kitchenRepository.findById(kitchenId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Kitchen",
	                            "id",
	                            kitchenId));

	    // Fetch all menu items of that kitchen
	    List<MenuItem> menuItems =
	            menuRepository.findByKitchenId(kitchen.getId());

	    // Convert Entity List -> DTO List
	    return menuItems.stream()
	            .map(menu -> {

	                MenuResponseDto response =
	                        modelMapper.map(menu,
	                                MenuResponseDto.class);

	                response.setKitchenId(
	                        menu.getKitchen().getId());

	                response.setKitchenName(
	                        menu.getKitchen().getKitchenName());

	                return response;

	            })
	            .toList();
	}
	@Override
	public MenuResponseDto getMenuItemById(Long id) {
		
		
	    MenuItem menuItem = menuRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Menu Item",
	                            "id",
	                            id));

	    MenuResponseDto menu = modelMapper.map(menuItem, MenuResponseDto.class);
	    
	    menu.setKitchenId(menuItem.getKitchen().getId());
	    menu.setKitchenName(menuItem.getKitchen().getKitchenName());
	    return menu;
	}
	
	
	
	
	
	@Override
	public MenuResponseDto updateMenu(Long id,
	        MenuRequestDto requestDto) {

	    MenuItem menuItem = menuRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Menu Item",
	                            "id",
	                            id));

	    menuItem.setDishName(requestDto.getDishName());
	    menuItem.setPrice(requestDto.getPrice());
	    menuItem.setFoodCategory(requestDto.getFoodCategory());
	    menuItem.setDescription(requestDto.getDescription());
	    menuItem.setImageUrl(requestDto.getImageUrl());
	    menuItem.setAvailable(requestDto.isAvailable());

	    MenuItem updatedMenu =
	            menuRepository.save(menuItem);

	    return modelMapper.map(updatedMenu,
	            MenuResponseDto.class);
	}
	@Override
	public void deleteMenuItem(Long id) {

	    MenuItem menuItem = menuRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Menu Item",
	                            "id",
	                            id));

	    menuRepository.delete(menuItem);
	}
	
	@Override
	public List<MenuResponseDto> getAvailableMenuItems(Long kitchenId) {

	    List<MenuItem> menuItems =
	            menuRepository.findByKitchenIdAndAvailableTrue(kitchenId);

	    return menuItems.stream()
	            .map((menuItem) ->{
	            	MenuResponseDto dto =  modelMapper.map(menuItem,
	                            MenuResponseDto.class);
	              dto.setKitchenId(menuItem.getKitchen().getId());
	              
	              dto.setKitchenName(menuItem.getKitchen().getKitchenName());
	              
	              return dto;
	                    
	            })
	            .toList();
	    
	    
	    
	    
	}
	
	@Override
	public MenuResponseDto changeAvailability(
	        Long id,
	        MenuAvailabilityDto dto) {

	    MenuItem menuItem = menuRepository.findById(id)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Menu Item",
	                            "id",
	                            id));

	    menuItem.setAvailable(dto.isAvailable());

	    MenuItem updatedMenu = menuRepository.save(menuItem);

	    MenuResponseDto response =
	            modelMapper.map(updatedMenu, MenuResponseDto.class);

	    response.setKitchenId(updatedMenu.getKitchen().getId());
	    response.setKitchenName(updatedMenu.getKitchen().getKitchenName());

	    return response;
	}
	
	@Override
	public List<MenuResponseDto> searchDish(String keyword) {

	    List<MenuItem> menuItems =
	            menuRepository.findByDishNameContainingIgnoreCase(keyword);

	    return menuItems.stream()
	            .map(menuItem -> {

	                MenuResponseDto dto =
	                        modelMapper.map(menuItem, MenuResponseDto.class);

	                dto.setKitchenId(menuItem.getKitchen().getId());
	                dto.setKitchenName(menuItem.getKitchen().getKitchenName());

	                return dto;
	            })
	            .toList();
	}
	@Override
	public List<MenuResponseDto> searchAvailableDish(String keyword) {

	    List<MenuItem> menuItems =
	            menuRepository.findByDishNameContainingIgnoreCaseAndAvailableTrue(keyword);

	    return menuItems.stream()
	            .map(menuItem -> {

	                MenuResponseDto dto =
	                        modelMapper.map(menuItem, MenuResponseDto.class);

	                dto.setKitchenId(menuItem.getKitchen().getId());
	                dto.setKitchenName(menuItem.getKitchen().getKitchenName());

	                return dto;
	            })
	            .toList();
	}
	@Override
	public List<MenuResponseDto> filterByPrice(
	        BigDecimal minPrice,
	        BigDecimal maxPrice) {

	    List<MenuItem> menuItems =
	            menuRepository.findByPriceBetweenAndAvailableTrue(
	                    minPrice,
	                    maxPrice);

	    return menuItems.stream()
	            .map(menuItem -> {

	                MenuResponseDto dto =
	                        modelMapper.map(menuItem,
	                                MenuResponseDto.class);

	                dto.setKitchenId(menuItem.getKitchen().getId());
	                dto.setKitchenName(menuItem.getKitchen().getKitchenName());

	                return dto;
	            })
	            .toList();
	}
	
	@Override
	public List<MenuResponseDto> getAllMenuItems() {

	    List<MenuItem> menuItems = menuRepository.findAll();

	    return menuItems.stream()
	            .map(menuItem -> {

	                MenuResponseDto dto =
	                        modelMapper.map(menuItem,
	                                MenuResponseDto.class);

	                dto.setKitchenId(menuItem.getKitchen().getId());

	                dto.setKitchenName(
	                        menuItem.getKitchen().getKitchenName());

	                return dto;
	            })
	            .toList();
	}
	
	
	
	
	
	
}
