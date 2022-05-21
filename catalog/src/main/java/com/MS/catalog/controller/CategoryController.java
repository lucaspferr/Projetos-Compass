package com.MS.catalog.controller;

import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        List<CategoryDTO> list = categoryService.getAllCategories();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        Category category = categoryService.createCategory(categoryDTO);
        return new ResponseEntity(category, HttpStatus.CREATED);
    }

    @GetMapping("/{category_id}/products")
    public ResponseEntity<?> getCategoryById(@PathVariable long category_id){
        Category category = categoryService.getCategoryById(category_id);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<?> updateCategory(@PathVariable long category_id, @RequestBody @Valid CategoryDTO categoryDTO){
        Category category = categoryService.updateCategory(category_id, categoryDTO);
        return new ResponseEntity(category, HttpStatus.CREATED);
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long category_id){
        categoryService.deleteCategory(category_id);
        return ResponseEntity.status(204).build();
    }


}
