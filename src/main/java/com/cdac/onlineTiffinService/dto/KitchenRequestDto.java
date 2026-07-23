package com.cdac.onlineTiffinService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KitchenRequestDto {
	@NotBlank(message = "Kitchen name is required")
	@Size(min = 3, max = 100, message = "Kitchen name must be between 3 and 100 characters")
	private String kitchenName;

	@NotBlank(message = "Address is required")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	private String address;

	@NotBlank(message = "City is required")
	@Size(max = 100, message = "City cannot exceed 100 characters")
	private String kitchenCity;
	
	
	@Size(max = 300, message = "Description cannot exceed 300 characters")
	private String description;

	
}
