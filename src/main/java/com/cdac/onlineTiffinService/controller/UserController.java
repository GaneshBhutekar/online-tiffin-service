package com.cdac.onlineTiffinService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import com.cdac.onlineTiffinService.dto.AuthRequest;
import com.cdac.onlineTiffinService.dto.AuthResp;
import com.cdac.onlineTiffinService.dto.UserRequestDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;
import com.cdac.onlineTiffinService.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController // @Controller - cls level + @ResponseBody - added implicitly on
//ret types of all request handling method
@RequestMapping("/users")
@RequiredArgsConstructor // Lombok - generates the ctor with final & non null fields
public class UserController {
	// dependency - constr based D.I
	private final UserService userService;

	/*
	 * Desc - User sign in 
	 * URL - http://host:port/users/signin 
	 * Method - POST (for
	 * security , JWT generation, JSON payload) 
	 * Eg . Patient Logs in 
	 * Payload - email , password (AuthRequest - DTO ) 
	 * Success Resp -SC 200 Auth Resp (name, message + JWT) 
	 * Failure Resp - SC 401 ApiResp DTO(status :
	 * succes | failure , timestamp , message)
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> userSignIn(@RequestBody @Valid AuthRequest request) {
		System.out.println("in user signin " + request);
	
			// invoke service layer
			AuthResp resp = userService.authenticateUser(request);
			return ResponseEntity.ok(resp);

	}

	/*
	 * Desc - Customer self sign up
	 * URL - http://host:port/users/signup
	 * Method - POST
	 * Payload - UserRequestDto (name, email, password, phoneNumber, city)
	 * Success Resp - SC 201 UserResponseDto
	 * Note - role is always forced to CUSTOMER regardless of what's sent
	 */
	@PostMapping("/signup")
	public ResponseEntity<UserResponseDto> userSignUp(@RequestBody @Valid UserRequestDto request) {
		UserResponseDto response = userService.registerCustomer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}

