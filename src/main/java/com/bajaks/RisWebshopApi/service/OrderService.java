package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.OrderCreateRequest;
import com.bajaks.RisWebshopApi.dto.OrderItemRequest;
import com.bajaks.RisWebshopApi.dto.OrderSearchData;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.OrderItem;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public Page<Order> search(OrderSearchData data) {
        Sort sort = Sort.by(Sort.Direction.DESC,"timestamp");
        Pageable pageable = PageRequest.of(data.getPage(),data.getPerPage(),sort);
        return orderRepository.search(data.getFrom(),data.getTo(),data.getAddress(), data.getMinTotal(), data.getMaxTotal(),data.getUser(),pageable);
    }

    public Order create(OrderCreateRequest request,User user) {
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            if (orderItemRequest.getQuantity() > orderItemRequest.getProduct().getStock()) {
                throw new RuntimeException("Product is out of stock");
            }
        }
        Order o = Order.builder().address(request.getAddress()).user(user).build();
        List<OrderItem> items = request.getItems().stream().map(orderItemRequest -> OrderItem.builder()
                .order(o)
                .unitPrice(orderItemRequest.getProduct().getPrice())
                .quantity(orderItemRequest.getQuantity())
                .product(orderItemRequest.getProduct()).build()).toList();
        o.setOrderItems(items);
        return o;
    }

    public Order delivered(Order order, Date date){
        order.setDelivered(true);
        order.setDeliveryTime(date);
        return orderRepository.save(order);
    }

    public Order delivered(Order order){
        return delivered(order,new Date());
    }
}
