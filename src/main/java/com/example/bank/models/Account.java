package com.example.bank.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
@Data
@Document
public class Account {
    @Id
    private String accountNumber;
    private String accountName;
    private BigDecimal bigDecimal;
    private String pin;
    private AccountTypes accountType;
}
