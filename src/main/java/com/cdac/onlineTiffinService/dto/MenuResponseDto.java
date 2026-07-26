package com.cdac.onlineTiffinService.dto;

import com.cdac.onlineTiffinService.model.FoodCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class MenuResponseDto {
    
    private Long id;
    
    
	
	private String dishName;

	
	private double price;

	
	private FoodCategory foodCategory;

	
	private boolean available = true;
	
	
	
	private String description;
	
	
	private String imageUrl;

	private Long KitchenId;
	
	private String kitchenName;
	
}
