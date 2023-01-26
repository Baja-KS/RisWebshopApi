package com.bajaks.WebshopProductAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderAttribute {
    private String name;
    private Sort.Direction direction;
}
