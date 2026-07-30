package com.cdac.onlineTiffinService.service;

import com.cdac.onlineTiffinService.dto.ApiResponse;
import com.cdac.onlineTiffinService.dto.AuthRequest;
import com.cdac.onlineTiffinService.dto.AuthResp;
import com.cdac.onlineTiffinService.dto.UserRequestDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;

public interface UserService {

	AuthResp authenticateUser(AuthRequest request);

	ApiResponse encrytPasswords();
	
	UserResponseDto registerCustomer(UserRequestDto request);

}
