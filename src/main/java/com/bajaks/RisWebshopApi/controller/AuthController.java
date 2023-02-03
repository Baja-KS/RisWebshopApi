package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.LoginRequest;
import com.bajaks.RisWebshopApi.dto.LoginResponse;
import com.bajaks.RisWebshopApi.dto.RegisterRequest;
import com.bajaks.RisWebshopApi.dto.RegisterResponse;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.security.UserDetailsImplementation;
import com.bajaks.RisWebshopApi.security.UserDetailsServiceImplementation;
import com.bajaks.RisWebshopApi.service.UserService;
import com.bajaks.RisWebshopApi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImplementation userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        UserDetailsImplementation userDetails = (UserDetailsImplementation) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        User user = userService.getByUsername(userDetails.getUsername());
        return LoginResponse.builder().token(jwtUtil.generateToken(userDetails)).user(user).build();
    }
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return userService.create(registerRequest);
    }
}
