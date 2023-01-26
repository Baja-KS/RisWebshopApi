package com.bajaks.WebshopProductAPI.service;

import com.bajaks.WebshopProductAPI.dto.OrderAttribute;
import com.bajaks.WebshopProductAPI.dto.ProductCreateDTO;
import com.bajaks.WebshopProductAPI.dto.SearchData;
import com.bajaks.WebshopProductAPI.dto.mapper.DTOMapper;
import com.bajaks.WebshopProductAPI.exception.CategoryNotFoundException;
import com.bajaks.WebshopProductAPI.exception.ProductNotFoundException;
import com.bajaks.WebshopProductAPI.model.Category;
import com.bajaks.WebshopProductAPI.model.Product;
 import com.bajaks.WebshopProductAPI.repository.ProductRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository pr;
    private final MinioService minioService;
    private final CategoryService categoryService;
    private final DTOMapper mapper;

    public Product getById(Long id){
        return pr.findById(id).orElseThrow(() -> new ProductNotFoundException(id.toString()));
    }
    public Product getById(Integer id){
        return pr.findById(Long.valueOf(id)).orElseThrow(() -> new ProductNotFoundException(id.toString()));
    }

    public List<Product> all(){
        return pr.findAll();
    }

    public Page<Product> filter(SearchData data){
        Sort sort = Sort.unsorted();
        for(OrderAttribute o : data.getOrderAttributes()){
            sort = sort.and(Sort.by(o.getDirection(),o.getName()));
        }
        Pageable pageable = PageRequest.of(data.getPage(),data.getPageSize(),sort);

        return pr.search(data.getSearch(),data.getCategory(), data.getMinPrice(), data.getMaxPrice(), data.getStock(),pageable);
    }

    public Product create(ProductCreateDTO dto, MultipartFile img){
        Product p = mapper.productCreateDTOToProduct(dto);
        Category category = categoryService.getById(dto.getCategoryId());

        if (!img.isEmpty()){
            try {
                minioService.upload(img);
            } catch (IOException | InsufficientDataException | ServerException | ErrorResponseException |
                     NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                     InternalException e) {
                throw new RuntimeException(e);
            }
            p.setImg(img.getOriginalFilename());
        }
        p.setCategory(category);
        return pr.save(p);
    }

}
