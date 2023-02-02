package com.bajaks.RisWebshopApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductUpdateDTO {
    private String name;
    private Float price;
    private String specification;
    private Float discount;
    private Integer stock;
    private Long categoryId;
}
