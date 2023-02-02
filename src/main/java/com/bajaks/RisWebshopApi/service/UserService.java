package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.LoginRequest;
import com.bajaks.RisWebshopApi.dto.LoginResponse;
import com.bajaks.RisWebshopApi.dto.RegisterRequest;
import com.bajaks.RisWebshopApi.dto.RegisterResponse;
import com.bajaks.RisWebshopApi.model.Role;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;


    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public RegisterResponse create(RegisterRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        userRepository.save(user);
        return RegisterResponse.builder().message("Registration successful").build();

    }
}
