package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.exception.ErrorResponse;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.service.OrderService;
import com.bajaks.RisWebshopApi.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@CrossOrigin("*")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> exception(RuntimeException exception) {
        log.atError().log(Arrays.toString(exception.getStackTrace()));
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<Object> jwtExpiredException(ExpiredJwtException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.UNAUTHORIZED);
    }

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
        System.out.println(user.getFirstName());
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

    @GetMapping("/report")
    public void report(HttpServletResponse r, @RequestParam(defaultValue = "") String address,
                       @RequestParam(required = false) Float minTotal,
                       @RequestParam(required = false) Float maxTotal,
                       @RequestParam(required = false)Date from,
                       @RequestParam(required = false)Date to) throws Exception {
        JasperPrint jasperPrint = orderService.orderReport(new OrderReportData(from,to,address,minTotal,maxTotal));
        r.setContentType("application/x-download");
        r.addHeader("Content-disposition", "attachment; filename=Orders.pdf");
        OutputStream out = r.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,out);
    }

}
