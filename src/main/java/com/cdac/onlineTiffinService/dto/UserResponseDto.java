package com.cdac.onlineTiffinService.dto;


import com.cdac.onlineTiffinService.model.Role;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponseDto {
	private Long id;


	private String name;


	private String email;



	private String phoneNumber;

	private Role role;

	private String city;

	private boolean enabled = true;

	
}
