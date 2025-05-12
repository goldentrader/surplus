package com.example.user_management.repository;

import com.example.user_management.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // You can add custom query methods here if needed
    User findByEmail(String email);
    // Or query by other attributes
    User findByIdAndEmail(String id, String email);
}

