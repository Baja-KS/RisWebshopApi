package com.bajaks.RisWebshopApi.security;

import com.bajaks.RisWebshopApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImplementation implements UserDetailsService{
    private final UserService userService;
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return UserDetailsImplementation.builder().user(userService.getByUsername(username)).build();
		
    }
 
     
 


	
     
}
