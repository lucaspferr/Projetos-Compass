package com.MS.history.repository;

import com.MS.history.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, Long> {

    @Query("{ 'history_id' : ?0 }")
    public User findByHistory_id(long history_id);

    @Query("{ 'user_id' : ?0 }")
    public User findByUser_id(long user_id);
}
