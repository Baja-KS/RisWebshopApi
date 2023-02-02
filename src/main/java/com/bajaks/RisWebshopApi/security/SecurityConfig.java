package com.bajaks.RisWebshopApi.security;

import com.bajaks.RisWebshopApi.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{


	private final UserDetailsServiceImplementation userDetailsService;
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/product/delete","/product/delete/**").hasRole("ADMINISTRATOR")
						.requestMatchers("/product/create","/product/update","/product/update/**").hasAnyRole("ADMINISTRATOR","EMPLOYEE")
//						.requestMatchers("/products/**").permitAll()
						.requestMatchers("/category/delete","/category/delete/**").hasRole("ADMINISTRATOR")
						.requestMatchers("/category/create","/category/update","/category/update/**").hasAnyRole("ADMINISTRATOR","EMPLOYEE")
//						.requestMatchers("/categories/**").permitAll()
						.requestMatchers("/user/**").hasRole("ADMINISTRATOR")
						.requestMatchers("/auth/login","/auth/register").anonymous()
						.anyRequest().permitAll()

				).addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
			throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder)
				.and()
				.build();
	}

}
