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
		UsernamePasswordAuthenticationToken holder = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		System.out.println("before " + holder.isAuthenticated());// false
		/*
		 * 1.2 call authenticate method - in case of failure Spring security throws
		 * AuthenticationException - un checked exception
		 */
		Authentication fullyAutheticatedDetails = authManager.authenticate(holder);
		// => authentication successful
		System.out.println("after  " + fullyAutheticatedDetails.isAuthenticated());// t
		System.out.println(fullyAutheticatedDetails.getPrincipal());// custom user details
		CustomUserDetailsImpl userDetails = (CustomUserDetailsImpl) fullyAutheticatedDetails.getPrincipal();

		/*
		 * 1.3 In case of successful authentication - create JWT & send it in auth
		 * response.
		 */

		return new AuthResp("Login Successful !", jwtUtils.generateJwt(userDetails));
	}

	@Override
	public ApiResponse encrytPasswords() {
		userRepo.findAll().forEach(user -> user.setPassword(encoder.encode(user.getPassword())));
		return new ApiResponse("Passwords encoded ...", "Success");
	}
	
	
	@Override
	public UserResponseDto registerCustomer(UserRequestDto request) {
		userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new DuplicateResourceException("User with this email already exists");
		});

		User user = mapper.map(request, User.class);
		user.setRole(Role.CUSTOMER); // force CUSTOMER role for self sign-up, ignore any role sent by client
		user.setPassword(encoder.encode(request.getPassword()));

		User savedUser = userRepo.save(user);
		
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
			System.err.println("Failed to send registration email: " + e.getMessage());
		}
		
		
		return mapper.map(savedUser, UserResponseDto.class);
	}

}