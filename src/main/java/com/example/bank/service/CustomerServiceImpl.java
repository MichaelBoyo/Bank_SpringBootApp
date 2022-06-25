package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Customer;
import com.example.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public String addCustomer(Customer customer) {
        customerRepository.save(customer);
        return "saved";
    }

    @Override
    public String createAccount(String customerId, Account account) {
        return null;
    }
}
