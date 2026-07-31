package com.cdac.onlineTiffinService.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.cdac.onlineTiffinService.dto.KitchenRequestDto;
import com.cdac.onlineTiffinService.dto.KitchenResponseDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationRequestDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationResponseDto;
import com.cdac.onlineTiffinService.model.Kitchen;

public interface KitchenService {
	UserKitchenRegistrationResponseDto createKitchen(UserKitchenRegistrationRequestDto req);
	
//	List<KitchenResponseDto> getAllKitchens();
	//pagination
	Page<KitchenResponseDto> getAllKitchens(int page, int size);
	
	// get kitchen by id
	KitchenResponseDto getKitchenById(Long id);
	
	//update the kitchen 
	
	UserKitchenRegistrationResponseDto updateKitchen(Long id , UserKitchenRegistrationRequestDto req);
	
	void deleteKitchen(Long id);
	
//	List<KitchenResponseDto> getKitchenByCity(String city);
	// pagination added
	Page<KitchenResponseDto> getKitchenByCity(
	        String city,
	        int page,
	        int size);
	
//	List<KitchenResponseDto> getKitchenByActive();
	
	// pagination added
	Page<KitchenResponseDto> getKitchenByActive(int page, int size);
	
	List<KitchenResponseDto> getKitchenByNotActive();
	KitchenResponseDto blockKitchen(Long id);
	KitchenResponseDto unblockKitchen(Long id);
	
}


