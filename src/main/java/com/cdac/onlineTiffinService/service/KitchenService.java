package com.cdac.onlineTiffinService.service;


import java.util.List;

import com.cdac.onlineTiffinService.dto.KitchenRequestDto;
import com.cdac.onlineTiffinService.dto.KitchenResponseDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationRequestDto;
import com.cdac.onlineTiffinService.dto.UserKitchenRegistrationResponseDto;
import com.cdac.onlineTiffinService.model.Kitchen;

public interface KitchenService {
	UserKitchenRegistrationResponseDto createKitchen(UserKitchenRegistrationRequestDto req);
	
	List<KitchenResponseDto> getAllKitchens();
	
	// get kitchen by id
	KitchenResponseDto getKitchenById(Long id);
	
	//update the kitchen 
	
	UserKitchenRegistrationResponseDto updateKitchen(Long id , UserKitchenRegistrationRequestDto req);
	
	void deleteKitchen(Long id);
	
	List<KitchenResponseDto> getKitchenByCity(String city);
	
	List<KitchenResponseDto> getKitchenByActive();
	List<KitchenResponseDto> getKitchenByNotActive();
	KitchenResponseDto blockKitchen(Long id);
	KitchenResponseDto unblockKitchen(Long id);
	
}


