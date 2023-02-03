package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.model.Role;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<User> search(UserSearchData data){
        Pageable pageable = PageRequest.of(data.getPage(),data.getPerPage());
        return userRepository.search(data.getSearch(),data.getRole(),pageable);
    }

    public User changeRole(User user,Role role){
        user.setRole(role);
        return userRepository.save(user);
    }

    public User changeRole(User user,String roleString){
        Role role = Role.USER;
        if (roleString.equalsIgnoreCase("administrator")){
            role = Role.ADMINISTRATOR;
        }
        if (roleString.equalsIgnoreCase("employee")){
            role = Role.EMPLOYEE;
        }

        return changeRole(user,role);
    }
}
