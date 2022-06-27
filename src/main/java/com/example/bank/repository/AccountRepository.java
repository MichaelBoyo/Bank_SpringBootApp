package com.example.bank.repository;

import com.example.bank.models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    Account findAccountByAccountNumber(String accountNo);
}
