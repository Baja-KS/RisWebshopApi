package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.OrderAttribute;
import com.bajaks.RisWebshopApi.dto.ProductCreateDTO;
import com.bajaks.RisWebshopApi.dto.ProductUpdateDTO;
import com.bajaks.RisWebshopApi.dto.SearchData;
import com.bajaks.RisWebshopApi.dto.mapper.DTOMapper;
import com.bajaks.RisWebshopApi.exception.ProductNotFoundException;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.model.Product;
 import com.bajaks.RisWebshopApi.repository.ProductRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    public Product update(Product product, ProductUpdateDTO dto, MultipartFile img){
        for (Field f : dto.getClass().getDeclaredFields()){
            String field = f.getName();
            if (field.equalsIgnoreCase("categoryId")){
                continue;
            }
            Method getter = null;
            Method setter = null;
            Method productGetter = null;
            Object oldItem = null;
            Object newItem = null;
            try {
                setter = product.getClass().getMethod("set"+ StringUtils.capitalize(field),f.getType());
                getter = dto.getClass().getMethod("get"+ StringUtils.capitalize(field));
                productGetter = product.getClass().getMethod("get"+ StringUtils.capitalize(field));
                newItem = getter.invoke(dto);
                oldItem = productGetter.invoke(product);
                log.atInfo().log("Old item {},new item {}",oldItem,newItem);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (newItem != null && !newItem.equals(oldItem)){
                log.atInfo().log("Property chaning --> Property: {} Old: {} New: {}",field,oldItem,newItem);
                try {
                    setter.invoke(product,newItem);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (img != null && !img.isEmpty() && (!img.getName().equals(product.getImg()))){
            try {
                minioService.upload(img);
            } catch (IOException | InsufficientDataException | ServerException | ErrorResponseException |
                     NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                     InternalException e) {
                throw new RuntimeException(e);
            }
            product.setImg(img.getOriginalFilename());
        }
        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(product.getCategory().getId())){
            Category category = categoryService.getById(dto.getCategoryId());
            product.setCategory(category);
        }
        return pr.save(product);
    }

    public void delete(Product product){
        pr.delete(product);
    }

}
