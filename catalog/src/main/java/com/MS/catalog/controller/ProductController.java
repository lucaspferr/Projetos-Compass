package com.MS.catalog.controller;


import com.MS.catalog.model.DTO.ProductDTO;
import com.MS.catalog.model.Product;
import com.MS.catalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getAllProducts(){
        List<Product> list = productService.getAllProducts();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable long product_id){
        Product product = productService.getProductById(product_id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{product_id}")
    public ResponseEntity<?> updateProduct(@PathVariable long product_id,@RequestBody @Valid ProductDTO productDTO){
        Product product = productService.updateProduct(product_id, productDTO);
        return new ResponseEntity(product, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO){
        Product product = productService.createProduct(productDTO);
        return new ResponseEntity(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long product_id){
        productService.deleteProduct(product_id);
        return ResponseEntity.status(204).build();
    }
}
