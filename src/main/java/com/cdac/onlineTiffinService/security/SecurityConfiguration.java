package com.cdac.onlineTiffinService.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration //declares a spring bean containing bean config  - equivalent to xml config file - to declare spring bean
@EnableWebSecurity //to specify the customization in Spring security
@EnableMethodSecurity //to enable method level authorization
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final CustomJWTVerificationFilter customJWTVerificationFilter;
	/*
	 * Configure spring bean to customize security filter chain
	 * 
	 */
	@Bean //<bean id ,class..../>
	SecurityFilterChain customizeSecurityFilterChain(HttpSecurity http) throws Exception
	{
		//1. Disable CSRF protection - since it's REST API - stateless
		http.csrf(csrf -> csrf.disable());
		//2. Disable HttpSession creation - Stateless 
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		//3. Configure Basic Authentication - with default
	//	http.httpBasic(Customizer.withDefaults());
		//4. Configure auth rules - URL based - public end points
		//IMPORTANT: Spring Security evaluates requestMatchers in declaration order and
		//uses the FIRST match. More specific / literal paths (e.g. "/api/menu/admin")
		//are therefore declared BEFORE generic single-segment wildcards (e.g. "/api/menu/{id}"),
		//otherwise the wildcard would shadow the literal path.
		http.authorizeHttpRequests(request -> 
		request
		// ---------- Swagger & Auth (PUBLIC) ----------
		.requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
		.requestMatchers(HttpMethod.POST,"/users/signin","/users/signup").permitAll() //tested

		// ---------- Kitchens: literal/specific paths first ----------
		.requestMatchers(HttpMethod.POST,"/api/kitchens").permitAll() //owner self-registration
		.requestMatchers(HttpMethod.GET,"/api/kitchens/notactive").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET,"/api/kitchens/active").permitAll()
		.requestMatchers(HttpMethod.GET,"/api/kitchens/city/{city}").permitAll()
		.requestMatchers(HttpMethod.PUT,"/api/kitchens/{id}/block","/api/kitchens/{id}/unblock").hasRole("ADMIN") // tested
		.requestMatchers(HttpMethod.GET,"/api/kitchens").permitAll()
		.requestMatchers(HttpMethod.GET,"/api/kitchens/{id}").permitAll() //generic - must come after the literal ones above
		.requestMatchers(HttpMethod.PUT,"/api/kitchens/{id}").hasAnyRole("KITCHEN","ADMIN")
		.requestMatchers(HttpMethod.DELETE,"/api/kitchens/{id}").hasAnyRole("KITCHEN","ADMIN")

		// ---------- Menu: literal/specific paths first ----------
		.requestMatchers(HttpMethod.GET,"/api/menu/kitchen/{kitchenId}/available").permitAll()
		.requestMatchers(HttpMethod.GET,"/api/menu/kitchen/{kitchenId}").permitAll()
		.requestMatchers(HttpMethod.POST,"/api/menu/kitchen/{kitchenId}").hasRole("KITCHEN")
		.requestMatchers(HttpMethod.GET,"/api/menu/customer/search").permitAll()
		.requestMatchers(HttpMethod.GET,"/api/menu/admin").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET,"/api/menu/search").hasAnyRole("KITCHEN","ADMIN")
		.requestMatchers(HttpMethod.GET,"/api/menu/filter").permitAll()
		.requestMatchers(HttpMethod.PATCH,"/api/menu/{id}/availability").hasRole("KITCHEN")
		.requestMatchers(HttpMethod.GET,"/api/menu/{id}").permitAll() //generic - must come after the literal ones above
		.requestMatchers(HttpMethod.PUT,"/api/menu/{id}").hasRole("KITCHEN")
		.requestMatchers(HttpMethod.DELETE,"/api/menu/{id}").hasRole("KITCHEN")

		// ---------- Orders: literal/specific paths first ----------
		.requestMatchers(HttpMethod.GET,"/orders/dashboard/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET,"/orders/customer/admin/{customerId}").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET,"/orders/customer/{customerId}/status/{status}").hasRole("CUSTOMER")
		.requestMatchers(HttpMethod.POST,"/orders/customer/{customerId}").hasRole("CUSTOMER")
		.requestMatchers(HttpMethod.GET,"/orders/customer/{customerId}").hasRole("CUSTOMER")
		.requestMatchers(HttpMethod.GET,"/orders/kitchen/{kitchenId}").hasAnyRole("KITCHEN","ADMIN")
		.requestMatchers(HttpMethod.PUT,"/orders/{orderId}/cancel").hasRole("CUSTOMER")
		.requestMatchers(HttpMethod.PUT,"/orders/{orderId}/accept","/orders/{orderId}/reject",
				"/orders/{orderId}/preparing","/orders/{orderId}/ready",
				"/orders/{orderId}/delivered").hasAnyRole("KITCHEN","ADMIN")
		.requestMatchers(HttpMethod.GET,"/orders").hasRole("ADMIN")
		//GET /orders/{orderId} intentionally left generic - falls through to "authenticated" below

		//Remaining endpoints (e.g. GET /orders/{orderId}) - any authenticated user
		.anyRequest().authenticated()
		);
		//add custom jwt filter - before 1st authentication filter - 
		http.addFilterBefore(customJWTVerificationFilter, UsernamePasswordAuthenticationFilter.class);
		//HttpSecurity - Builder to build sec filter chain.
		return http.build();
	}
	
	//Configure Spring Security supplied PasswordEncoder as spring bean
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	//configure Spring Security supplied AuthenticationManager as spring bean
   //provider - AuthConfig
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	
	
	

}

