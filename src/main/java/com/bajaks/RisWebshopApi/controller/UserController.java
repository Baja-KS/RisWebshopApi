package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.LoginRequest;
import com.bajaks.RisWebshopApi.dto.LoginResponse;
import com.bajaks.RisWebshopApi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;


}
