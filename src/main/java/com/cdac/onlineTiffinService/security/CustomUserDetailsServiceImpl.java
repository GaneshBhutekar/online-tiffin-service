package com.cdac.onlineTiffinService.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.onlineTiffinService.model.User;
import com.cdac.onlineTiffinService.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("**** in load user **** {} ",email );
		User user=userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User by email doesn't exist!!!!!!!!"));
		//user - exists
		return new CustomUserDetailsImpl(user);
	}

}
