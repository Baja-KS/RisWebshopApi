package com.bajaks.WebshopProductAPI.controller;

import com.bajaks.WebshopProductAPI.dto.CategoryCreateDTO;
import com.bajaks.WebshopProductAPI.dto.CategoryDTO;
import com.bajaks.WebshopProductAPI.dto.MessageResponse;
import com.bajaks.WebshopProductAPI.dto.mapper.DTOMapper;
import com.bajaks.WebshopProductAPI.model.Category;
import com.bajaks.WebshopProductAPI.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO create(@Valid @RequestBody CategoryCreateDTO dto){
        return categoryService.create(dto);
    }

    @PutMapping(value = "/update/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO update(@Valid @RequestBody CategoryCreateDTO dto,@PathVariable(name = "id")Category category){
        return categoryService.update(category,dto);
    }



    @GetMapping
    public List<CategoryDTO> all(){
        return categoryService.all();
    }

    @GetMapping("/details/{id}")
    public CategoryDTO get(@PathVariable(name = "id") Long id){
        return categoryService.getDTOById(id);
    }

    @DeleteMapping("/delete/{id}")
    public MessageResponse delete(@PathVariable(name = "id") Category category){
        categoryService.delete(category);
        return new MessageResponse("Delete successful!");
    }
}
