package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.Account;
import com.example.bank.models.Customer;
import com.example.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;
    private static int uid = 0;
    @Override
    public Customer addCustomer(Customer customer) {
        customer.setCustomerNo(String.valueOf(++uid));
        return customerRepository.save(customer);
    }

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
        Optional<Customer> customer = Optional.ofNullable(customerRepository.findCustomerByCustomerNo(customerID));
        return customer.orElseThrow(()-> {
            throw new BankException("customer not found");
        });
    }
}
