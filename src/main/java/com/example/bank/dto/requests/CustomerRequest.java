package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class CustomerRequest {
    private int year;
    private int month;
    private int day;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
}
