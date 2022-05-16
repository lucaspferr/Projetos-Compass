package com.MS.catalog.controller;

import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CategoryDTO> getAllCategories(){
        return this.categoryService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return ResponseEntity.ok().body(this.categoryService.createCategory(categoryDTO));
    }

    @GetMapping("/{category_id}/products")
    public Category getCategoryById(@PathVariable long category_id){
        return categoryService.getCategoryById(category_id);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<?> updateCategory(@PathVariable long category_id, @RequestBody @Valid CategoryDTO categoryDTO){
        categoryService.updateCategory(category_id, categoryDTO);
        return null;
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long category_id){
        categoryService.deleteCategory(category_id);
        return null;
    }


}
