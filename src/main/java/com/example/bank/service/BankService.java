package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;

import java.util.List;

public interface BankService {
    String createBank(Bank bank);
    String addCustomer(String bankNo,Customer customer);

    String addAccount(String bankNo,Account account, String customerID,String pin);

    Customer getCustomer(String bankNo, String customerId);

    List<Customer> getAllCustomers(String bankNo);

    String deposit(String bankNo, String customerNo, String accountNo, double amount);

    Bank getBank(String bankId);

    String withdraw(String bankNo, String customerNo, String accountNo, double amount);

    String transfer(String bankNo, String customerNo, String senderAccountNo, String receiverAccountNo, double amount);

    Account getAccount(String bankNo, String customerID, String accountID);
}
