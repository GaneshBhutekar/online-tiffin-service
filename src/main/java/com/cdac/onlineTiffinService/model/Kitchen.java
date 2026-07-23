package com.cdac.onlineTiffinService.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="kitchens")
public class Kitchen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Kitchen name is required")
	@Column(nullable = false, length = 100,name="kitchen_name")
	private String kitchenName;

	@NotBlank(message = "Address is required")
	@Column(nullable = false, length = 255)
	private String address;

	@NotBlank(message = "City is required")
	@Column(nullable = false, length = 50,name="kitchen_city")
	private String kitchenCity;

	@Column(length = 500)
	private String description;

	@Column(nullable = false,name="average_rating")
	private Double averageRating = 0.0;

	@Column(nullable = false)
	private int ratingCount = 0;

	@Column(nullable = false)
	private boolean blocked = false;
	
	// association 
	@OneToOne
	@JoinColumn(name="owner_id" , nullable=false,unique=true)
	@ToString.Exclude
	private User owner;
	
	// relation with menuitem 
	@OneToMany(mappedBy = "kitchen",
				cascade=CascadeType.ALL, // if parent get affected , it will affect the child too...
				orphanRemoval = true // remove relation so delete the child
				)
	private List<MenuItem> menuItems = new ArrayList<>();
	
	@OneToMany(mappedBy = "kitchen") // here we dont want to have to use cascade cause kitchen does not own orders
	private List<Orders> orders = new ArrayList<>();
	
	
	
	
}
