package com.example.bank.service;

import com.example.bank.models.Account;

public interface AccountService {
    String addAccount(Account account);
    String getBalance(String accountNumber);
    String deposit(String accountNumber,int amount);
    String withdraw(String accountNumber,int amount);
    String transfer(Account account,String accountNumber, int amount);
}
