package com.MS.catalog.controller;


import com.MS.catalog.model.DTO.ProductDTO;
import com.MS.catalog.model.Product;
import com.MS.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(){
        return this.productService.getAllProducts();
    }

    @GetMapping("/{product_id}")
    public Product getProductById(@PathVariable long product_id){
        return this.productService.getProductById(product_id);
    }

    @PutMapping("/{product_id}")
    public Product updateProduct(@PathVariable long product_id,@RequestBody @Valid ProductDTO productDTO){
        return productService.updateProduct(product_id, productDTO);
    }

    @PostMapping
    public Product createProduct(@RequestBody @Valid ProductDTO productDTO){
        return this.productService.createProduct(productDTO);
    }

    @DeleteMapping("/{product_id}")
    public void deleteProduct(@PathVariable long product_id){
        productService.deleteProduct(product_id);
    }
}
