package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;

    @Override
    public String addAccount(Account account) {
        accountRepository.save(account);
        return "saved successfully";
    }

    @Override
    public String getBalance(String accountNumber) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        return String.valueOf(account.getBalance());
    }

    @Override
    public String deposit(String accountNumber,double amount) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        return "deposit successful";
    }

    @Override
    public String withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        return "withdraw success";
    }

    @Override
    public String transfer(Account account,String accountNumber,double amount) {
        return null;
    }
}
