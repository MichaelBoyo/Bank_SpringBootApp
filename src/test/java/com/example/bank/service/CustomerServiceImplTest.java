package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.Customer;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountServiceImpl accountService;
    @InjectMocks
    CustomerServiceImpl customerService;
    @Test
    void addCustomer() {
        var customer = new Customer();
        customerService.addCustomer(customer);
        var customerArgCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgCaptor.capture());
        var savedCustomer = customerArgCaptor.getValue();
        assertNotNull(savedCustomer.getCustomerNo());
        assertNotNull(savedCustomer.getDateCreated());
    }



    @Test
    void getCustomer() {
        var customer = new Customer();
        customerService.addCustomer(customer);
        var date = customer.getDateCreated();
        var customerArgCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgCaptor.capture());
        var savedCustomer = customerArgCaptor.getValue();

        assertEquals(date,savedCustomer.getDateCreated());
    }

}