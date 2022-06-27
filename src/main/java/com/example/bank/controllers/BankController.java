package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;
import com.example.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bank")
public class BankController {
    @Autowired
    BankService bankService;

    @PostMapping("/createBank")
    public String createBank(@RequestBody Bank bank) {
        return bankService.createBank(bank);
    }

    @GetMapping("/viewBank/{bankId}")
    public Bank getBank(@PathVariable String bankId) {
        return bankService.getBank(bankId);
    }

    @PostMapping("/addCustomer/{bankNo}")
    public String addCustomer(@PathVariable String bankNo, @RequestBody Customer customer) {
        return bankService.addCustomer(bankNo, customer);
    }

    @PostMapping("/addAccount/{bankNo}/{customerId}/{pin}")
    public String addAccount(@PathVariable String bankNo, @RequestBody Account account,
                             @PathVariable String customerId, @PathVariable String pin) {
        return bankService.addAccount(bankNo, account, customerId, pin);
    }

    @GetMapping("/getCustomer/{bankNo}/{customerId}")
    public Customer getCustomer(@PathVariable String bankNo, @PathVariable String customerId) {
        return bankService.getCustomer(bankNo, customerId);
    }

    @GetMapping("/getAllCustomers/{bankNo}")
    public List<Customer> getAllCustomers(@PathVariable String bankNo) {
        return bankService.getAllCustomers(bankNo);
    }

    @PatchMapping("/deposit/{bankNo}/{customerNo}/{accountNo}/{amount}")
    public String deposit(@PathVariable String accountNo,
                          @PathVariable String bankNo, @PathVariable String customerNo,
                          @PathVariable double amount) {
        return bankService.deposit(bankNo, customerNo, accountNo, amount);
    }

    @PatchMapping("/withdraw/{bankNo}/{customerNo}/{accountNo}/{amount}/{pin}")
    public String withdraw(@PathVariable String accountNo,
                           @PathVariable String bankNo, @PathVariable String customerNo,
                           @PathVariable double amount, @PathVariable String pin) {
        return bankService.withdraw(bankNo, customerNo, accountNo, amount, pin);
    }

    @PatchMapping("/transfer/{bankNo}/{customerNo}/{senderAccountNo}/{receiverAccountNo}/{amount}/{pin}")
    public String transfer(@PathVariable String bankNo,
                           @PathVariable String customerNo,
                           @PathVariable String senderAccountNo,
                           @PathVariable String receiverAccountNo,
                           @PathVariable double amount, @PathVariable String pin) {
        return bankService.transfer(bankNo, customerNo, senderAccountNo, receiverAccountNo, amount,pin);
    }
    @GetMapping("/getAccount/{bankNo}/{customerNo}/{accountNo}")
    public Account getAccount(@PathVariable String accountNo, @PathVariable String bankNo,
                              @PathVariable String customerNo){
        return bankService.getAccount(bankNo,customerNo,accountNo);
    }
    @GetMapping("/getBalance/{bankNo}/{customerNo}/{accountNo}")
    public String getBalance(@PathVariable String accountNo,
                             @PathVariable String bankNo, @PathVariable String customerNo){
        return bankService.getBalance(bankNo,customerNo,accountNo);
    }


}
