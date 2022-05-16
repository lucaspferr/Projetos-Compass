package com.MS.history.repository;

import com.MS.history.model.History;
import com.MS.history.model.Purchases;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<History, Long> {

    @Query("{ 'history_id' : ?0 }")
    public History findByHistory_id(long history_id);


}
