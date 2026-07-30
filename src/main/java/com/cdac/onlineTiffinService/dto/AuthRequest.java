package com.cdac.onlineTiffinService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthRequest {
	@NotBlank(message="Email is required , can't be black")
	@Email(message="Invalid email format")
	private String email;
	
	
	private String password;
}
