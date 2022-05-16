package com.MS.history.repository;

import com.MS.history.model.Purchases;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchases, Long> {

    @Query("{ 'history_id' : ?0 }")
    public List<Purchases> getPurchases(long history_id);

    @Query("{ 'purchase_id' : ?0 }")
    public Purchases getPurchasesByPurchaseId(long purchase_id);
}
