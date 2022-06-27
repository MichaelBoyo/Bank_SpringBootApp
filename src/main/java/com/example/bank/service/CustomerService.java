package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Customer;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    String createAccount(String customerId, Account account);

    Customer getCustomer(String customerID);
}
