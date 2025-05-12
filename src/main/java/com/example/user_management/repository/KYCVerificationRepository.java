package com.example.user_management.repository;

import com.example.user_management.entity.KYCVerification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KYCVerificationRepository extends MongoRepository<KYCVerification, String> {
    // You can add custom query methods here if needed
    KYCVerification findByUserId(String userId);
}
