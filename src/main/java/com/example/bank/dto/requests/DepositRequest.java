package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class DepositRequest {
    private String accountNo;
    private String bankNo;
    private String customerNo;
    private Double amount;
}
