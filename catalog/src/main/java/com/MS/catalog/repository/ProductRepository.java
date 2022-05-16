package com.MS.catalog.repository;


import com.MS.catalog.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {

    @Query("{ 'product_id' : ?0 }")
    public Product findByProduct_id(long product_id);

    @Query("{ 'category_ids' : ?0 }")
    public List<Product> findByCategory_ids(Long category_ids);

}
