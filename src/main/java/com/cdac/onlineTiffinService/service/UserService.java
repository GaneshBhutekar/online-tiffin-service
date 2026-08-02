package com.cdac.onlineTiffinService.service;

import com.cdac.onlineTiffinService.dto.ApiResponse;
import com.cdac.onlineTiffinService.dto.AuthRequest;
import com.cdac.onlineTiffinService.dto.AuthResp;
import com.cdac.onlineTiffinService.dto.UserRequestDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;
import com.cdac.onlineTiffinService.dto.ForgotPasswordRequest;
import com.cdac.onlineTiffinService.dto.ResetPasswordRequest;
public interface UserService {

	AuthResp authenticateUser(AuthRequest request);

	ApiResponse encrytPasswords();
	
	UserResponseDto registerCustomer(UserRequestDto request);
	
	ApiResponse forgotPassword(ForgotPasswordRequest request);

	ApiResponse resetPassword(ResetPasswordRequest request);
	
	

}
