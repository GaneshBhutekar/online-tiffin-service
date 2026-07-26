package com.cdac.onlineTiffinService.dto;

import com.cdac.onlineTiffinService.model.FoodCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class MenuRequestDto {
    
    
	@NotBlank(message = "Dish name is required")
	private String dishName;

	@Positive(message = "Price must be greater than 0")
	private double price;

	@NotNull(message = "Food category is required")
	private FoodCategory foodCategory;
	
	private String description;
	
	private String imageUrl;
	
	
	private boolean available;

}
