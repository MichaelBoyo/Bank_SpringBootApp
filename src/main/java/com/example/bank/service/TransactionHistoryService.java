package com.example.bank.service;

import com.example.bank.models.TransactionHistory;

public interface TransactionHistoryService {
    TransactionHistory addTransaction(TransactionHistory history);
    void getTransaction(String transactionId);

    TransactionHistory depositHistory(double amount);
}
