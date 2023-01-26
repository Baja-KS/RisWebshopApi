package com.bajaks.WebshopProductAPI.dto;

import com.bajaks.WebshopProductAPI.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchData {
    private String search;
    private Category category;
    private Float minPrice;
    private Float maxPrice;
    private Integer page;
    private Integer pageSize;
    private Integer stock;
    private List<OrderAttribute> orderAttributes;

}
