package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.OrderItem;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.OrderRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final MinioService minioService;
    private final ProductService productService;

    public Page<Order> search(OrderSearchData data) {
        Sort sort = Sort.by(Sort.Direction.DESC,"timestamp");
        Pageable pageable = PageRequest.of(data.getPage(),data.getPerPage(),sort);
        return orderRepository.search(data.getFrom(),data.getTo(),data.getAddress(), data.getMinTotal(), data.getMaxTotal(),data.getUser(),pageable);
    }

    public Order create(OrderCreateRequest request,User user) {
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            Product p = productService.getById(orderItemRequest.getProductId());
            if (orderItemRequest.getQuantity() > p.getStock()) {
                throw new RuntimeException("Product is out of stock");
            }
        }
        Order o = Order.builder().address(request.getAddress()).user(user).build();
        List<OrderItem> items = request.getItems().stream().map(orderItemRequest -> {
            Product p = productService.getById(orderItemRequest.getProductId());
            productService.removeFromStock(p,orderItemRequest.getQuantity());
            return OrderItem.builder()
                    .order(o)
                    .unitPrice(p.getPrice())
                    .quantity(orderItemRequest.getQuantity())
                    .product(p).build();
        }).toList();
        o.setOrderItems(items);
        log.atInfo().log("Items ordered: {}",o.getOrderItems());
        orderRepository.save(o);

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

    public JasperPrint orderReport(OrderReportData data){
        List<OrderReportItem> orders = orderRepository.forReport(data.getFrom(),data.getTo(),data.getAddress(),data.getMinTotal(),data.getMaxTotal()).stream().map(order -> OrderReportItem.builder()
                .username(order.getUser().getUsername())
                .orderTime(order.getTimestamp())
                .total(order.total())
                .deliveryTime(order.getDeliveryTime())
                .isDelivered((order.getDelivered() != null && order.getDelivered()) ? "Yes" : "No")
                .address(order.getAddress()).build()).toList();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
        try {
            InputStream template = minioService.get("jasper-templates/order-report-template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            template.close();
            return JasperFillManager.fillReport(jasperReport,data.toMap(),dataSource);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
