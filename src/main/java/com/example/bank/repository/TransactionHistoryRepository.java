package com.example.bank.repository;

import com.example.bank.models.TransactionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory,String> {
    TransactionHistory findTransactionHistoryByTransactionId(String id);
}
