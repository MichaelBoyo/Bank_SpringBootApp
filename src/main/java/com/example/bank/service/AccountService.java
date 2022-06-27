package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;

import java.math.BigDecimal;

public interface AccountService {
    Account addAccount(Account account);
    BigDecimal getBalance(String accountNumber);
    String deposit(Account account,TransactionHistory history);
    String withdraw(String accountNumber,double amount, TransactionHistory history);

    Account getAccount(String accountID);
}
