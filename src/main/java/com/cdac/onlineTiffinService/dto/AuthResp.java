package com.cdac.onlineTiffinService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResp {	
	private String message;
	private String jwt;

}