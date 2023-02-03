package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.*;
import com.bajaks.RisWebshopApi.exception.ErrorResponse;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.Review;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.service.ProductService;
import com.bajaks.RisWebshopApi.service.ReviewService;
import com.bajaks.RisWebshopApi.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@CrossOrigin("*")
@Slf4j
public class ProductController {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> exception(RuntimeException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<Object> jwtExpiredException(ExpiredJwtException exception) {
        log.atError().log(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(),new Date()), HttpStatus.UNAUTHORIZED);
    }

    private final ProductService productService;
    private final ReviewService reviewService;

    private final UserService userService;

    @GetMapping("/details/{id}")
    public Product getById(@PathVariable(name = "id") Long id){
        return productService.getById(id);
    }

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product create(@RequestParam String name,@RequestParam(required = false) String specification,@RequestParam Float price,
           @RequestParam(required = false) Float discount,@RequestParam Long categoryId,@RequestParam(required = false) Integer stock
            , @RequestPart(value = "img",required = false)MultipartFile img){
        log.atInfo().log("Category: {}",categoryId);

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
        return productService.filter(ProductSearchData.builder()
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

    @PostMapping("/review/{id}")
    public MessageResponse review(@PathVariable(name = "id")Product product, Authentication authentication,@RequestBody ReviewRequest request) {
        log.atInfo().log("rating: {},comment: {}",request.getRating(),request.getComment());
        User user = userService.getByUsername(authentication.getName());
        return reviewService.add(product,user,request);
    }
    @GetMapping("/review/{id}")
    public Page<Review> forProduct(@PathVariable(name = "id")Product product,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "5") Integer perPage){
        return reviewService.forProduct(product,page,perPage);

    }
    @GetMapping("/reviews/report/{id}")
    public void report(HttpServletResponse r,@PathVariable(name = "id")Product product) throws Exception {
        JasperPrint jasperPrint = reviewService.reviewReport(product);
        r.setContentType("application/x-download");
        r.addHeader("Content-disposition", "attachment; filename=Reviews.pdf");
        OutputStream out = r.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,out);
    }
}
