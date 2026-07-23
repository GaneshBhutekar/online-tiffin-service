package com.cdac.onlineTiffinService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter



public class KitchenResponseDto {
	private Long id;

	private String kitchenName;


	private String address;

	private String kitchenCity;

	private String description;

	private Double averageRating = 0.0;

	private int ratingCount = 0;

	private boolean blocked = false;
}
