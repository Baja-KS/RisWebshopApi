package com.bajaks.WebshopProductAPI.controller;

import com.bajaks.WebshopProductAPI.dto.*;
import com.bajaks.WebshopProductAPI.model.Category;
import com.bajaks.WebshopProductAPI.model.Product;
import com.bajaks.WebshopProductAPI.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@CrossOrigin("http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/details/{id}")
    public Product getById(@PathVariable(name = "id") Long id){
        return productService.getById(id);
    }

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product create(@RequestParam String name,@RequestParam(required = false) String specification,@RequestParam Float price,
           @RequestParam(required = false) Float discount,@RequestParam Long categoryId,@RequestParam(required = false) Integer stock
            , @RequestPart(value = "img",required = false)MultipartFile img){

        return productService.create(ProductCreateDTO.builder()
                        .categoryId(categoryId)
                        .name(name)
                        .discount(discount)
                        .price(price)
                        .specification(specification)
                        .stock(stock)
                .build(), img);
    }

    @PutMapping(value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product update(@PathVariable(name = "id")Product product,@RequestParam(required = false) String name,@RequestParam(required = false) String specification,@RequestParam(required = false) Float price,
                          @RequestParam(required = false) Float discount,@RequestParam(required = false) Long categoryId,@RequestParam(required = false) Integer stock
            , @RequestPart(value = "img",required = false)MultipartFile img){

        return productService.update(product, ProductUpdateDTO.builder()
                .categoryId(categoryId)
                .name(name)
                .discount(discount)
                .price(price)
                .specification(specification)
                .stock(stock)
                .build(), img);
    }
    @GetMapping("/search")
    public Page<Product> search(@RequestParam(defaultValue = "") String search,
                                @RequestParam(required = false) Float minPrice,
                                @RequestParam(required = false) Float maxPrice,
                                @RequestParam(required = false) Category category,
                                @RequestParam(required = false) Integer stock,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "5") Integer perPage){
        return productService.filter(SearchData.builder()
                .search(search)
                        .minPrice(minPrice)
                        .maxPrice(maxPrice)
                        .category(category).stock(stock)
                        .page(page)
                        .pageSize(perPage)
                        .orderAttributes(List.of(new OrderAttribute("name", Sort.Direction.ASC)))
                    .build());
    }

    @DeleteMapping("/delete/{id}")
    public MessageResponse delete(@PathVariable(name = "id") Product product){
        productService.delete(product);
        return new MessageResponse("Delete successful!");
    }
}
