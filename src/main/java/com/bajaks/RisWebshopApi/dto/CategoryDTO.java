package com.bajaks.RisWebshopApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Integer productCount;
}
