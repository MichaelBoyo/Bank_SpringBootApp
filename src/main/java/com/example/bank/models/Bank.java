package com.example.bank.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@Document
public class Bank {
    @Id
    private String bankId;
    private String bankName;
    @DBRef
    private List<Customer> customers = new ArrayList<>();
}
