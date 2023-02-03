package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.CategoryCreateDTO;
import com.bajaks.RisWebshopApi.dto.CategoryDTO;
import com.bajaks.RisWebshopApi.dto.mapper.DTOMapper;
import com.bajaks.RisWebshopApi.exception.CategoryNotFoundException;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.repository.CategoryRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository cr;
    private final MinioService minioService;
    private final DTOMapper mapper;

    public List<CategoryDTO> all(String sortDirection){
        return cr.findAll(Sort.by(Sort.Direction.fromString(sortDirection),"name")).stream().map(mapper::categoryToCategoryDTO).toList();
    }
    public List<CategoryDTO> all(){
        return this.all("asc");
    }

//    public Optional<Category> getById(Integer id){
//        return cr.findById(Long.valueOf(id));
//    }
    public Category getById(Long id){

        return cr.findById(id).orElseThrow(CategoryNotFoundException::new);
    }
    public CategoryDTO getDTOById(Long id){
        return mapper.categoryToCategoryDTO(this.getById(id));
    }

    public CategoryDTO create(CategoryCreateDTO dto){
        Category c = cr.save(mapper.categoryCreateDTOToCategory(dto));
        return CategoryDTO.builder().id(c.getId()).name(c.getName()).build();
    }

    public CategoryDTO update(Category category,CategoryCreateDTO dto){
        if (!dto.getName().isBlank()){
            category.setName(dto.getName());
        }
        Category c = cr.save(category);
        return CategoryDTO.builder().name(c.getName()).id(c.getId()).build();
    }

    public void delete(Category category){
        cr.delete(category);
    }
    

}
