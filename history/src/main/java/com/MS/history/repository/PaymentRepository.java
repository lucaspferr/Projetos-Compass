package com.MS.history.repository;

import com.MS.history.model.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentMethod, Long> {

    @Query("{ 'purchase_id' : ?0 }")
    public PaymentMethod findByPurchase_id(long purchase_id);
}
