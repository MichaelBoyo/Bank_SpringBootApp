package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;
import com.example.bank.service.AccountService;
import com.example.bank.service.BankService;
import com.example.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank")
public class BankController {
    @Autowired
    BankService bankService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @PostMapping("/createBank")
    public String createBank(@RequestBody Bank bank){
        return bankService.createBank(bank);
    }

    @PostMapping("/addCustomer/{bankId}")
    public String addCustomer(@PathVariable String bankId, @RequestBody Customer customer){
        customerService.addCustomer(customer);
        return bankService.addCustomer(bankId,customer);
    }
    @PostMapping("/addAccount/{bankId}/{customerId}")
    public String addAccount(@PathVariable String bankId,@RequestBody Account account, @PathVariable String customerId){
        return bankService.addAccount(bankId,account,customerId);
    }
    @GetMapping("/getCustomer/{bankId}/{customerId}")
    public Customer getCustomer(@PathVariable String bankId, @PathVariable String customerId){
        return bankService.getCustomer(bankId,customerId);
    }
    @GetMapping("/getAllCustomers/{bankId}")
    public List<Customer> getAllCustomers(@PathVariable String bankId){
        return bankService.getAllCustomers(bankId);
    }

}
