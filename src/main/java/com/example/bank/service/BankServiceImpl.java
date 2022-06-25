package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;
import com.example.bank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class BankServiceImpl implements BankService{
    @Autowired
    BankRepository bankRepository;

    @Override
    public String createBank(Bank bank) {
        bankRepository.save(bank);
        return "created successfully";
    }

    @Override
    public String addCustomer(String bankId,Customer customer) {
        Bank bank = bankRepository.findBanksByBankId(bankId);
        bank.getCustomers().add(customer);
        return "added successfully";
    }

    @Override
    public String addAccount(String bankId,Account account, String customerID) {
        Bank bank = bankRepository.findBanksByBankId(bankId);
        List<Customer>customers = bank.getCustomers();
       for(Customer customer: customers){
           if (customer.getId().equals(customerID)){
               customer.getAccounts().add(account);
           }
       }
        return "added successfully";
    }

    @Override
    public Customer getCustomer(String bankId, String customerId) {
        Bank bank = bankRepository.findBanksByBankId(bankId);
        List<Customer>customers = bank.getCustomers();
        for(Customer customer: customers){
            if (customer.getId().equals(customerId)){
                return customer;
            }
        }
        throw  new IllegalArgumentException("customer not found");
    }

    @Override
    public List<Customer> getAllCustomers(String bankId) {
        return bankRepository.findBanksByBankId(bankId).getCustomers();
    }
}
