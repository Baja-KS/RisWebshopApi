package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.OrderAttribute;
import com.bajaks.RisWebshopApi.dto.OrderCreateRequest;
import com.bajaks.RisWebshopApi.dto.OrderSearchData;
import com.bajaks.RisWebshopApi.dto.ProductSearchData;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.service.OrderService;
import com.bajaks.RisWebshopApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/create")
    public Order create(@RequestBody OrderCreateRequest request, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return orderService.create(request,user);
    }

    @PutMapping("/deliver/{id}")
    public Order deliver(@PathVariable(name = "id") Order order){
        return orderService.delivered(order);
    }

    @GetMapping("/search/admin")
    public Page<Order> search(@RequestParam(defaultValue = "") String address,
                              @RequestParam(required = false) Float minTotal,
                              @RequestParam(required = false) Float maxTotal,
                              @RequestParam(required = false) User user,
                              @RequestParam(required = false)Date from,
                              @RequestParam(required = false)Date to,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer perPage){
        OrderSearchData data = OrderSearchData.builder()
                .address(address)
                .minTotal(minTotal)
                .maxTotal(maxTotal)
                .user(user)
                .from(from)
                .to(to)
                .page(page)
                .perPage(perPage)
                .build();
        return orderService.search(data);
    }

    @GetMapping("/search/user")
    public Page<Order> search(@RequestParam(defaultValue = "") String address,
                              @RequestParam(required = false) Float minTotal,
                              @RequestParam(required = false) Float maxTotal,
                              @RequestParam(required = false)Date from,
                              @RequestParam(required = false)Date to,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer perPage,
                              Principal principal){

        User user = userService.getByUsername(principal.getName());
        OrderSearchData data = OrderSearchData.builder()
                .address(address)
                .minTotal(minTotal)
                .maxTotal(maxTotal)
                .user(user)
                .from(from)
                .to(to)
                .page(page)
                .perPage(perPage)
                .build();
        return orderService.search(data);
    }

}
