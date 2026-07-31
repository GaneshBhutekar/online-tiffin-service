package com.cdac.onlineTiffinService.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageRequest;

import com.cdac.onlineTiffinService.dto.MenuAvailabilityDto;
import com.cdac.onlineTiffinService.dto.MenuRequestDto;
import com.cdac.onlineTiffinService.dto.MenuResponseDto;
import com.cdac.onlineTiffinService.exceptions.ForbiddenException;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;
import com.cdac.onlineTiffinService.model.FoodCategory;
import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.model.MenuItem;
import com.cdac.onlineTiffinService.repository.KitchenRepository;
import com.cdac.onlineTiffinService.repository.MenuRepository;
import com.cdac.onlineTiffinService.specification.MenuSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{
	
	public final MenuRepository menuRepository;
	public final KitchenRepository kitchenRepository;
	
	private final ModelMapper modelMapper;

	// The JWT filter stores the logged-in user's id as the Authentication principal
	// (see CustomJWTVerificationFilter). We use that to verify ownership below -
	// role checks alone (hasRole("KITCHEN")) only prove someone owns *a* kitchen,
	// not that they own *this* kitchen / menu item.
	private Long getCurrentUserId() {
		return (Long) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
	}

	private void verifyKitchenOwnership(Kitchen kitchen) {
		Long currentUserId = getCurrentUserId();
		if (kitchen.getOwner() == null
				|| !kitchen.getOwner().getId().equals(currentUserId)) {
			throw new ForbiddenException(
					"You are not authorized to modify this kitchen's menu.");
		}
	}

	@Override
	public MenuResponseDto addMenuItem(Long kitchenId, MenuRequestDto dto) {
		Kitchen kitchen =
		        kitchenRepository.findById(kitchenId)
		        .orElseThrow(()->
		            new ResourceNotFoundException(
		                "Kitchen",
		                "id",
		                kitchenId));

		verifyKitchenOwnership(kitchen);

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
//	@Override
//	public Page<MenuResponseDto> getMenuItemsByKitchen(Long kitchenId,int page,int size) {
//
//	    // Check whether kitchen exists
//	    Kitchen kitchen = kitchenRepository.findById(kitchenId)
//	            .orElseThrow(() ->
//	                    new ResourceNotFoundException(
//	                            "Kitchen",
//	                            "id",
//	                            kitchenId));
//
//	    Pageable pageable = PageRequest.of(page, size);
//	    // Fetch all menu items of that kitchen
//	    Page<MenuItem> menuItems =
//	            menuRepository.findByKitchenId(kitchen.getId(),pageable);
//
//	    // Convert Entity List -> DTO List
////	    return menuItems.stream()
////	            .map(menu -> {
////
////	                MenuResponseDto response =
////	                        modelMapper.map(menu,
////	                                MenuResponseDto.class);
////
////	                response.setKitchenId(
////	                        menu.getKitchen().getId());
////
////	                response.setKitchenName(
////	                        menu.getKitchen().getKitchenName());
////
////	                return response;
////
////	            })
////	            .toList();
//	    
//	    return menuItems.map(menu -> {
//
//	        MenuResponseDto response =
//	                modelMapper.map(
//	                        menu,
//	                        MenuResponseDto.class);
//
//	        response.setKitchenId(
//	                menu.getKitchen().getId());
//
//	        response.setKitchenName(
//	                menu.getKitchen().getKitchenName());
//
//	        return response;
//	    });
//	}
	
    @Override
    public Page<MenuResponseDto> getMenuItemsByKitchen(

            Long kitchenId,

            int page,

            int size,

            FoodCategory category,

            Boolean available,

            BigDecimal minPrice,

            BigDecimal maxPrice,

            String sortBy,

            String direction

    ) {
    	Kitchen kitchen = kitchenRepository.findById(kitchenId)
    	        .orElseThrow(() ->
    	                new ResourceNotFoundException(
    	                        "Kitchen","id",kitchenId
    	                ));

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<MenuItem> specification =
                MenuSpecification.filter(
                        kitchenId,
                        category,
                        available,
                        minPrice,
                        maxPrice
                );

        Page<MenuItem> menuPage =
                menuRepository.findAll(specification, pageable);

        return menuPage.map(menu -> {

            MenuResponseDto dto =
                    modelMapper.map(menu, MenuResponseDto.class);

            dto.setKitchenId(menu.getKitchen().getId());

            dto.setKitchenName(menu.getKitchen().getKitchenName());

            return dto;
        });

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

	    verifyKitchenOwnership(menuItem.getKitchen());

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

	    verifyKitchenOwnership(menuItem.getKitchen());

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

	    verifyKitchenOwnership(menuItem.getKitchen());

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
