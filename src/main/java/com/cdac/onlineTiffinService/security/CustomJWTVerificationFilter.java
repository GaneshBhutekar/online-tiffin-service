package com.cdac.onlineTiffinService.security;


import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJWTVerificationFilter extends OncePerRequestFilter {
	private final JwtUtils jwtUtils;// used for JWT validation

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("******in custom filter *******");
		try {
			/*
			 * 1. Check if JWT exists in the incoming request - Authorization
			 */
			String headerValue = request.getHeader("Authorization"); // Bearer JWT
			if (headerValue != null && headerValue.startsWith("Bearer ")) {
				String jwt = headerValue.substring(7);
				/*
				 * 2. Validate token
				 */
				log.info("*****JWT {}",jwt);
				Claims claims = jwtUtils.validateToken(jwt);
				/*
				 * 3. Extract (user id & role ) from the payload
				 */
				Long userId = claims.get("user_id", Long.class);
				String userRole = claims.get("user_role", String.class);
				/*
				 * 3. Save these user details (user id & role ) in the Authentication object
				 * UsernamePasswordAuthenticationToken(Object principal, Object credentials,
				 * Collection<GrantedAuthority> authorities)
				 */
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null,
						List.of(new SimpleGrantedAuthority("ROLE_" + userRole)));
				/*
				 * Save this auth object under security context
				 */
				SecurityContextHolder.getContext().setAuthentication(auth);

			} else {
				log.info("*** NO JWT found !!!!!");
			}
			//continue with the remaining filter chain
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			// clear sec context
			SecurityContextHolder.clearContext();
			// JWT validation exception -> send SC 401 , with suitable error resp
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// 401
			response.getWriter().print("Invalid JWT !!!!!!!!!!!");
			return;
		}

	}

}
