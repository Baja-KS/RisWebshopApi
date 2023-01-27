package com.bajaks.WebshopProductAPI.dto.mapper;

import com.bajaks.WebshopProductAPI.dto.CategoryCreateDTO;
import com.bajaks.WebshopProductAPI.dto.CategoryDTO;
import com.bajaks.WebshopProductAPI.dto.ProductCreateDTO;
import com.bajaks.WebshopProductAPI.dto.ProductUpdateDTO;
import com.bajaks.WebshopProductAPI.model.Category;
import com.bajaks.WebshopProductAPI.model.Product;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public Category categoryCreateDTOToCategory(CategoryCreateDTO categoryCreateDTO){
        return Category.builder()
                .name(categoryCreateDTO.getName())
                .build();
    }
    public CategoryDTO categoryToCategoryDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .productCount(category.getProducts().size())
                .build();
    }
    public Product productCreateDTOToProduct(ProductCreateDTO productCreateDTO){
        return Product.builder()
                .name(productCreateDTO.getName())
                .price(productCreateDTO.getPrice())
                .discount(productCreateDTO.getDiscount())
                .stock(productCreateDTO.getStock())
                .specification(productCreateDTO.getSpecification())
                .build();
    }
    public Product productCreateDTOToProduct(ProductUpdateDTO productCreateDTO){
        return Product.builder()
                .name(productCreateDTO.getName())
                .price(productCreateDTO.getPrice())
                .discount(productCreateDTO.getDiscount())
                .stock(productCreateDTO.getStock())
                .specification(productCreateDTO.getSpecification())
                .build();
    }

}
