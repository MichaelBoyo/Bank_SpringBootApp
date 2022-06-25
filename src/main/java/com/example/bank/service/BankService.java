package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;

import java.util.List;

public interface BankService {
    String createBank(Bank bank);
    String addCustomer(String bankId,Customer customer);

    String addAccount(String bankId,Account account, String customerID);

    Customer getCustomer(String bankId, String customerId);

    List<Customer> getAllCustomers(String bankId);
}
