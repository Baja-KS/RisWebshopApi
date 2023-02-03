package com.bajaks.RisWebshopApi.dto;

import com.bajaks.RisWebshopApi.model.OrderItem;
import com.bajaks.RisWebshopApi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderCreateRequest {
    private String address;
    private List<OrderItemRequest> items;
}
