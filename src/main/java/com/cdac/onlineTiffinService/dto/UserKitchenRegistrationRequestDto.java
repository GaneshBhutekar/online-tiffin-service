package com.cdac.onlineTiffinService.dto;

import com.cdac.onlineTiffinService.model.Kitchen;
import com.cdac.onlineTiffinService.model.User;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserKitchenRegistrationRequestDto {
	private UserRequestDto user;
	
	private KitchenRequestDto kitchen;
}	
