package com.example.bank.service;

import com.example.bank.dto.requests.AccountDto;
import com.example.bank.dto.requests.BankRequest;
import com.example.bank.dto.requests.CustomerRequest;
import com.example.bank.dto.response.AccountResp;
import com.example.bank.dto.response.BankResponse;
import com.example.bank.dto.response.CustomerResponse;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface BankService {
    void createBank(BankRequest bank);
    void addCustomer(String bankNo, CustomerRequest customer);

    void addAccount(String bankNo, AccountDto account, String customerID, String pin);

    CustomerResponse getCustomer(String bankNo, String customerId);

    List<Customer> getAllCustomers(String bankNo);

    void deposit(String bankNo, String customerNo, String accountNo, double amount);

    BankResponse getBank(String bankId);

    void withdraw(String bankNo, String customerNo, String accountNo, double amount, String pin);

    void transfer(String bankNo, String customerNo,
                    String senderAccountNo, String receiverAccountNo, double amount, String pin);

    AccountResp getAccount(String bankNo, String customerID, String accountID);

    BigDecimal getBalance(String bankNo, String customerNo, String accountNo);
}
