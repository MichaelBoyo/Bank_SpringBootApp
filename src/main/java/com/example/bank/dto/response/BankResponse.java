package com.example.bank.dto.response;

import com.example.bank.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BankResponse {
    private String bankNo;
    private String bankName;
    private int numberOfCustomers;
    private List<Customer> customers;
}
