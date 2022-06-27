package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;

import java.math.BigDecimal;

public interface AccountService {
    Account addAccount(Account account, String pin);
    BigDecimal getBalance(Account account);
    String deposit(Account account,TransactionHistory history);
    String withdraw(Account account,TransactionHistory history, String pin);

    Account getAccount(String accountID);
}
