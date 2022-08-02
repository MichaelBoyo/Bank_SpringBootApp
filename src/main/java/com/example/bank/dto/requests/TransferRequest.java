package com.example.bank.dto.requests;

import lombok.Data;

@Data
public class TransferRequest {
    private String bankNo;
    private String customerNo;
    private String senderAccountNo;
    private String receiverAccountNo;
    private Double amount;
    private String pin;
}
