package com.example.bank.repository;

import com.example.bank.models.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void findAccountByAccountNumber() {
        var account = new Account();
        account.setAccountNumber("1");
        var account2 = new Account();
        account2.setAccountNumber("2");
        accountRepository.save(account);
        accountRepository.save(account2);

        var retrievedAccount = accountRepository.findAccountByAccountNumber("1");
        assertNotNull(retrievedAccount);
        assertEquals("1", retrievedAccount.getAccountNumber());
    }

}