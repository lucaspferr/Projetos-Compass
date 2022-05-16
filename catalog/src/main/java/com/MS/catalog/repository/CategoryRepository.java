package com.MS.catalog.repository;

import com.MS.catalog.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, Long> {

    @Query("{ 'category_id' : ?0 }")
    public Category findByCategory_id(long category_id);
    @Query(value = "{ 'category_id' : ?0 }", delete = true)
    public Category deleteByCategory_id(long category_id);

}
