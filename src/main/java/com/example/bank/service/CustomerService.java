package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Customer;

public interface CustomerService {
    String addCustomer(Customer customer);
    String createAccount(String customerId, Account account);

}
