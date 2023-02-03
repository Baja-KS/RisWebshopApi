package com.bajaks.RisWebshopApi.dto;

import com.bajaks.RisWebshopApi.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;

}
