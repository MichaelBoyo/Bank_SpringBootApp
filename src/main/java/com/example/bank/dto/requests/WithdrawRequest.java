package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class WithdrawRequest {
    private String accountNo;
    private String bankNo;
    private String customerNo;
    private Double amount;
    private String pin;

}
