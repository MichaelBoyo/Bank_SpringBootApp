package com.example.bank.dto.response;

import com.example.bank.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BankResponse {
    private String bankId;
    private String bankName;
    private int numberOfCustomers;
    private String dateCreated;
    private List<Customer> customers;
}
