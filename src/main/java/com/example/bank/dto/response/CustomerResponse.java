package com.example.bank.dto.response;

import com.example.bank.models.Account;
import com.example.bank.models.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerResponse {
    private String name;
    private String age;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private List<Account> accounts;
}
