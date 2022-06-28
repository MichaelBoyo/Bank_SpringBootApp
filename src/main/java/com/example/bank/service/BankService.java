package com.example.bank.service;

import com.example.bank.dto.requests.AccountDto;
import com.example.bank.dto.requests.CustomerRequest;
import com.example.bank.dto.response.AccountResp;
import com.example.bank.dto.response.BankResponse;
import com.example.bank.dto.response.CustomerResponse;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;

import java.util.List;

public interface BankService {
    String createBank(Bank bank);
    String addCustomer(String bankNo, CustomerRequest customer);

    String addAccount(String bankNo, AccountDto account, String customerID, String pin);

    CustomerResponse getCustomer(String bankNo, String customerId);

    List<Customer> getAllCustomers(String bankNo);

    String deposit(String bankNo, String customerNo, String accountNo, double amount);

    BankResponse getBank(String bankId);

    String withdraw(String bankNo, String customerNo, String accountNo, double amount, String pin);

    String transfer(String bankNo, String customerNo,
                    String senderAccountNo, String receiverAccountNo, double amount, String pin);

    AccountResp getAccount(String bankNo, String customerID, String accountID);

    String getBalance(String bankNo, String customerNo, String accountNo);
}
