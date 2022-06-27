package com.example.bank.models;

import com.example.bank.exceptions.BankException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
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
    private List<TransactionHistory> transactionHistory;
    private boolean validatePin(String pin){
        if(this.pin.equals(pin)){
            return true;
        }
        throw new BankException("incorrect pin");
    }
}
