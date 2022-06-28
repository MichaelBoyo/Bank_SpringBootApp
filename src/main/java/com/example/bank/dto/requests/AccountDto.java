package com.example.bank.dto.requests;

import lombok.Data;


@Data
public class AccountDto {
    private String accountName;
    private String pin;
    private String accountType;
}
