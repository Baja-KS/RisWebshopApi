package com.bajaks.WebshopProductAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductCreateDTO {
    @NotBlank
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private Float price;
    private String specification;
    private Float discount;
    private Integer stock;

    @NotNull
    private Long categoryId;

}
