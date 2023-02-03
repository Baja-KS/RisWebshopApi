package com.bajaks.RisWebshopApi.controller;

import com.bajaks.RisWebshopApi.dto.CategoryCreateDTO;
import com.bajaks.RisWebshopApi.dto.CategoryDTO;
import com.bajaks.RisWebshopApi.dto.MessageResponse;
import com.bajaks.RisWebshopApi.model.Category;
import com.bajaks.RisWebshopApi.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO create(@Valid @RequestBody CategoryCreateDTO dto){
        return categoryService.create(dto);
    }

    @PutMapping(value = "/update/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO update(@Valid @RequestBody CategoryCreateDTO dto,@PathVariable(name = "id")Category category){
//        category.getProducts().contains()
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

//    @GetMapping("/report")
//    public void report(HttpServletResponse r) throws Exception {
//        JasperPrint jasperPrint = categoryService.categoryReport();
//        r.setContentType("application/x-download");
//        r.addHeader("Content-disposition", "attachment; filename=Kategorije.pdf");
//        OutputStream out = r.getOutputStream();
//        JasperExportManager.exportReportToPdfStream(jasperPrint,out);
//    }
}
