package com.example.bank.dto.requests;

import lombok.Data;
@Data
public class GetBalanceRequest {
    private String accountNo;
    private  String bankNo;
    private  String customerNo;
}
