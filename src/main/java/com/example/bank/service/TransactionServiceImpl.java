package com.example.bank.service;

import com.example.bank.models.TransactionHistory;
import com.example.bank.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionHistoryService{
    @Autowired
    TransactionHistoryRepository txRepo;
    @Override
    public TransactionHistory addTransaction(TransactionHistory history) {
      return txRepo.save(history);
    }

    @Override
    public void getTransaction(String transactionId) {
        int a = 0;
    }
}
