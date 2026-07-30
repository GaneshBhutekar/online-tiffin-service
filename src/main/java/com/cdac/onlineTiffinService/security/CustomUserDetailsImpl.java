package com.cdac.onlineTiffinService.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cdac.onlineTiffinService.model.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetailsImpl implements UserDetails {
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {		
		/*
		 * Rets List of 
		 *  - instance of SimpleGrantedAuthority(String roleName)
		 *  - SimpleGrantedAuthority implements GrantedAuthority
		 */
		return List.of(new SimpleGrantedAuthority(user.getRole().name()));
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	public User getUser() {
		return user;
	}
	

}
