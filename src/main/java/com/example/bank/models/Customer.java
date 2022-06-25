package com.example.bank.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Customer {
    @Id
    private String id;
    private  String year;
    private  String month;
    private  String day;
    private  String firstName;
    private  String lastName;
    private  Gender gender;
    private  String email;
    private  String phoneNumber;
    private String age;
    @DBRef
    private List<Account> accounts = new ArrayList<>();
}
