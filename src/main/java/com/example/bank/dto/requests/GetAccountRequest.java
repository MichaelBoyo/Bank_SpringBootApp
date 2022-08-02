package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class GetAccountRequest {
    private String accountNo;
    private String bankNo;
    private String customerNo;
}
