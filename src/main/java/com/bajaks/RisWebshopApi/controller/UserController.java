package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.exception.ErrorResponse;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin("*")
@Slf4j
public class UserController {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> exception(RuntimeException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<Object> jwtExpiredException(ExpiredJwtException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.UNAUTHORIZED);
    }

    private final UserService userService;

    @GetMapping("/search")
    public Page<User> search(@RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer perPage){
        UserSearchData data = UserSearchData.builder()
                .search(search)
                .page(page)
                .perPage(perPage)
                .build();
        return userService.search(data);
    }

    @PutMapping("/role/{id}")
    public User changeRole(@PathVariable(name = "id") User user, @RequestBody ChangeRoleRequest request){
        return userService.changeRole(user,request.getRole());
    }


}
