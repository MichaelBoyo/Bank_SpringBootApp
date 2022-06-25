package com.example.bank.repository;

import com.example.bank.models.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {
    Bank findBanksByBankId(String id);

}
