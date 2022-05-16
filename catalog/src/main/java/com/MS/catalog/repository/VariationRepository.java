package com.MS.catalog.repository;

import com.MS.catalog.model.Variation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationRepository extends MongoRepository<Variation, Long> {

    @Query("{ 'variant_id' : ?0 }")
    public Variation findByVariant_id(long variant_id);

    @Query("{ 'product_id' : ?0 }")
    public List<Variation> findByProduct_id(long product_id);

    @Query(value = "{ 'product_id' : ?0 }", delete = true)
    public List<Variation> deleteByProduct_id(long product_id);

    @Query(value = "{ 'variant_id' : ?0 }", delete = true)
    public List<Variation> deleteByVariant_id(long variant_id);
}
