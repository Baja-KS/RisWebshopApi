package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
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
