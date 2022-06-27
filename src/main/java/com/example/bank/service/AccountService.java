package com.example.bank.service;

import com.example.bank.models.Account;

public interface AccountService {
    String addAccount(Account account);
    String getBalance(String accountNumber);
    String deposit(String accountNumber,double amount);
    String withdraw(String accountNumber,double amount);
    String transfer(Account account,String accountNumber, double amount);
}
