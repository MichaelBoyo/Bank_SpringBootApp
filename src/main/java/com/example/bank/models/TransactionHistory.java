package com.example.bank.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
public class TransactionHistory {
    @Id
    private String id;
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime date;
    private String sender;
    private String  receiver;
    public TransactionHistory(String transactionId, BigDecimal amount,
                              TransactionType type, LocalDateTime date,
                              String sender, String receiver) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
    }
    public TransactionHistory(){}
}
