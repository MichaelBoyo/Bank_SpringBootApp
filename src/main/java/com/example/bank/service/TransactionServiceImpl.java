package com.example.bank.service;

import com.example.bank.models.TransactionHistory;
import com.example.bank.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TransactionServiceImpl implements TransactionHistoryService{
    @Autowired
    TransactionHistoryRepository txRepo;
    @Override
    public TransactionHistory addTransaction(TransactionHistory history) {
        history.setTransactionId(txNo());
      return txRepo.save(history);
    }
    private String txNo(){
        Random random = new Random(10);
        StringBuilder txNo = new StringBuilder();
        txNo.append(101);
        for (int i = 0; i <27 ; i++) {
            txNo.append(random.nextInt(10));
        }
        return txNo.toString();
    }

    @Override
    public void getTransaction(String transactionId) {
        int a = 0;
    }


    @Override
    public TransactionHistory depositHistory(double amount) {
        return null;
    }
}
