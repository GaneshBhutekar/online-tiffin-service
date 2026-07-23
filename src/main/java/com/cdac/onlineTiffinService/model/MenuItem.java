package com.cdac.onlineTiffinService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="menu_items")

public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
	@NotBlank(message = "Dish name is required")
	@Column(name = "dish_name",nullable = false,length = 100)
	private String dishName;

	@Positive(message = "Price must be greater than 0")
	@Column(nullable = false)
	private double price;

	@NotNull(message = "Food category is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "food_category",nullable = false)
	private FoodCategory foodCategory;

	@Column(nullable = false)
	private boolean available = true;
	
	
	@Column(length = 300)
	private String description;
	
	@Column(name = "image_url" , length = 500)
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kitchen_id", nullable = false)
	@ToString.Exclude
	private Kitchen kitchen;
	
	
}
