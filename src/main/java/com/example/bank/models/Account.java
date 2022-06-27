package com.example.bank.models;

import com.example.bank.exceptions.BankException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Account {
    @Id
    private String id;
    private String accountNumber;
    private String accountName;
    private String pin;
    private AccountTypes accountType;
    @DBRef
    private List<TransactionHistory> transactionHistory = new ArrayList<>();
    public boolean pinIsValid(String pin){
        return this.pin.equals(pin);
    }
}
