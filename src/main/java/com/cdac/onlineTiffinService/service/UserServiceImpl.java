package com.cdac.onlineTiffinService.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.onlineTiffinService.dto.ApiResponse;
import com.cdac.onlineTiffinService.dto.AuthRequest;
import com.cdac.onlineTiffinService.dto.AuthResp;
import com.cdac.onlineTiffinService.dto.UserRequestDto;
import com.cdac.onlineTiffinService.dto.UserResponseDto;
import com.cdac.onlineTiffinService.exceptions.DuplicateResourceException;
import com.cdac.onlineTiffinService.model.Role;
import com.cdac.onlineTiffinService.model.User;
import com.cdac.onlineTiffinService.repository.UserRepository;
import com.cdac.onlineTiffinService.security.CustomUserDetailsImpl;
import com.cdac.onlineTiffinService.security.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import com.cdac.onlineTiffinService.dto.ForgotPasswordRequest;
import com.cdac.onlineTiffinService.dto.ResetPasswordRequest;

import com.cdac.onlineTiffinService.exceptions.BadRequestException;
import com.cdac.onlineTiffinService.exceptions.ResourceNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	// constr based D.I
	private final UserRepository userRepo;
	private final ModelMapper mapper;
	private final PasswordEncoder encoder;
	private final AuthenticationManager authManager;
	private final JwtUtils jwtUtils;
	private final EmailService emailService;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	@Override
	public AuthResp authenticateUser(AuthRequest request) {
		/*
		 * 1. Invoke Spring security supplied AuthenticationManager's - authenticate
		 * method API of AuthenticationManager interface public Authentication
		 * authenticate(Authentication auth); Authentication i/f - implemented by
		 * UserNamePasswordAuthenticationToken(Object username|email , Object
		 * credentials) 1.1 - Create UserNamePasswordAuthenticationToken , to hold email
		 * & password
		 */
		log.info("Login attempt for email: {}", request.getEmail());
		UsernamePasswordAuthenticationToken holder = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		log.debug("Authentication token created, authenticated={}", holder.isAuthenticated());// false
		/*
		 * 1.2 call authenticate method - in case of failure Spring security throws
		 * AuthenticationException - un checked exception
		 */
		Authentication fullyAutheticatedDetails = authManager.authenticate(holder);
		// => authentication successful
		log.debug("Authentication result, authenticated={}", fullyAutheticatedDetails.isAuthenticated());// t
		CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) fullyAutheticatedDetails.getPrincipal();

		/*
		 * 1.3 In case of successful authentication - create JWT & send it in auth
		 * response.
		 */
		log.info("Login successful for email: {}", request.getEmail());
		return new AuthResp("Login Successful !", jwtUtils.generateJwt(userDetails));
	}

	@Override
	public ApiResponse encrytPasswords() {
		userRepo.findAll().forEach(user -> user.setPassword(encoder.encode(user.getPassword())));
		return new ApiResponse("Passwords encoded ...", "Success");
	}
	
	
	@Override
	public UserResponseDto registerCustomer(UserRequestDto request) {
		log.info("Registering new customer with email: {}", request.getEmail());
		userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
			log.warn("Registration failed - email already exists: {}", request.getEmail());
			throw new DuplicateResourceException("User with this email already exists");
		});

		User user = mapper.map(request, User.class);
		user.setRole(Role.CUSTOMER); // force CUSTOMER role for self sign-up, ignore any role sent by client
		user.setPassword(encoder.encode(request.getPassword()));

		User savedUser = userRepo.save(user);
		log.info("Customer registered successfully with id: {}", savedUser.getId());
		
		try {
			emailService.sendEmail(
					savedUser.getEmail(),
					"Welcome to Online Tiffin Service!",
					"Hi " + savedUser.getName() + ",\n\n"
							+ "Your customer account has been registered successfully with Online Tiffin Service.\n"
							+ "You can now browse kitchens and start placing orders.\n\n"
							+ "Thanks,\nOnline Tiffin Service Team"
			);
		} catch (Exception e) {
			// do not fail registration if email sending fails
			log.error("Failed to send registration email to {}: {}", savedUser.getEmail(), e.getMessage(), e);
		}
		
		
		return mapper.map(savedUser, UserResponseDto.class);
	}
	
	@Override
	public ApiResponse forgotPassword(ForgotPasswordRequest request) {

	    log.info("Password reset OTP requested for email: {}", request.getEmail());

	    User user = userRepo.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User",
	                            "email",
	                            request.getEmail()
	                    )
	            );

	    String otp = String.valueOf(
	            100000 + SECURE_RANDOM.nextInt(900000)
	    );

	    user.setResetOtp(otp);
	    user.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));

	    userRepo.save(user);

	    emailService.sendEmail(
	            user.getEmail(),
	            "Online Tiffin Service Password Reset OTP",
	            "Hi " + user.getName() + ",\n\n"
	                    + "Your password reset OTP is: " + otp + "\n\n"
	                    + "This OTP is valid for 10 minutes.\n"
	                    + "Do not share this OTP with anyone.\n\n"
	                    + "Thanks,\n"
	                    + "Online Tiffin Service Team"
	    );

	    log.info("Password reset OTP sent successfully to: {}", user.getEmail());

	    return new ApiResponse(
	            "Password reset OTP sent successfully",
	            "Success"
	    );
	}
	
	@Override
	public ApiResponse resetPassword(ResetPasswordRequest request) {

	    log.info("Password reset attempt for email: {}", request.getEmail());

	    User user = userRepo.findByEmail(request.getEmail())
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "User",
	                            "email",
	                            request.getEmail()
	                    )
	            );

	    if (user.getResetOtp() == null
	            || !user.getResetOtp().equals(request.getOtp())) {

	        throw new BadRequestException("Invalid OTP");
	    }

	    if (user.getResetOtpExpiry() == null
	            || LocalDateTime.now().isAfter(user.getResetOtpExpiry())) {

	        throw new BadRequestException(
	                "OTP has expired. Please request a new OTP"
	        );
	    }

	    user.setPassword(
	            encoder.encode(request.getNewPassword())
	    );

	    user.setResetOtp(null);
	    user.setResetOtpExpiry(null);

	    userRepo.save(user);

	    log.info("Password reset successfully for email: {}", user.getEmail());

	    return new ApiResponse(
	            "Password reset successfully",
	            "Success"
	    );
	}
	

}