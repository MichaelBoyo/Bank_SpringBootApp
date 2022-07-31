package com.example.bank.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Customer {
    @Id
    private String id;
    private String customerNo;
    private LocalDate dob;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String dateCreated;
    @DBRef
    private List<Account> accounts = new ArrayList<>();
    public Customer(LocalDate dob, String firstName, String lastName, Gender gender, String email, String phoneNumber) {
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
