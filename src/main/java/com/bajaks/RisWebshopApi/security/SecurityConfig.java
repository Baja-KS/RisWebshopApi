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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH","OPTIONS"));
//		configuration.setAllowCredentials(true);
		//the below three lines will add the relevant CORS response headers
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	private final UserDetailsServiceImplementation userDetailsService;
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().configurationSource(corsConfigurationSource());
		http
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/users/**","/orders/search/admin","/orders/report").hasRole("ADMINISTRATOR")
						.requestMatchers("/products/create","/products/update","/products/update/**","/products/delete/**").hasAnyRole("ADMINISTRATOR","EMPLOYEE")
						.requestMatchers("/categories/create","/categories/update","/categories/update/**","/categories/delete/**").hasAnyRole("ADMINISTRATOR","EMPLOYEE")
						.requestMatchers("/orders/deliver/**","/products/reviews/report/**").hasAnyRole("ADMINISTRATOR","EMPLOYEE")
						.requestMatchers("/products/review/**","/orders/create","/orders/search/user").authenticated()
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
