package com.example.bank.dto.requests;

import lombok.Data;


@Data
public class GetCustomerDto {
    private String bankNo;
    private String customerId;
}
