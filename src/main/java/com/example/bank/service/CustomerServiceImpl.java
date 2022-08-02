package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.Account;
import com.example.bank.models.Customer;
import com.example.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public Customer addCustomer(Customer customer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM--dd HH-mm");
        customer.setDateCreated(LocalDateTime.now().format(formatter));
        return customerRepository.save(customer);
    }
    //todo: validate customer details using combinator pattern

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public String createAccount(String customerId, Account account) {
        return null;
    }

    @Override
    public Customer getCustomer(String customerID) {
        Optional<Customer> customer = customerRepository.findById(customerID);
        return customer.orElseThrow(()-> {
            throw new BankException("customer not found");
        });
    }
}
