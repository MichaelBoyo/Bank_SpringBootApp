package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class CustomerRequest {
    private Integer year;
    private Integer month;
    private Integer day;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String bankNo;
}
