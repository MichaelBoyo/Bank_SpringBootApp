package com.example.bank.controllers;

import com.example.bank.dto.requests.AccountDto;
import com.example.bank.dto.requests.BankRequest;
import com.example.bank.dto.requests.CustomerRequest;
import com.example.bank.dto.response.AccountResp;
import com.example.bank.dto.response.BankResponse;
import com.example.bank.dto.response.CustomerResponse;
import com.example.bank.exceptions.BankException;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;
import com.example.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/bank")
public class BankController {
    @Autowired
    BankService bankService;

    @PostMapping("/createBank")
    public ResponseEntity<?> createBank(@RequestBody BankRequest bank) {
        try {
            bankService.createBank(bank);
            return new ResponseEntity<>("bank created successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewBank/{bankId}")
    public BankResponse getBank(@PathVariable String bankId) {
        return bankService.getBank(bankId);
    }

    @PostMapping("/addCustomer/{bankNo}")
    public ResponseEntity<?> addCustomer(@PathVariable String bankNo, @RequestBody CustomerRequest customer) {
        try {
            bankService.addCustomer(bankNo, customer);
            return new ResponseEntity<>("customer added successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addAccount/{bankNo}/{customerId}/{pin}")
    public ResponseEntity<?> addAccount(@PathVariable String bankNo, @RequestBody AccountDto accountDto,
                             @PathVariable String customerId, @PathVariable String pin) {
        try {
            bankService.addAccount(bankNo, accountDto, customerId, pin);
            return new ResponseEntity<>("account added successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCustomer/{bankNo}/{customerId}")
    public CustomerResponse getCustomer(@PathVariable String bankNo, @PathVariable String customerId) {
        return bankService.getCustomer(bankNo, customerId);
    }

    @GetMapping("/getAllCustomers/{bankNo}")
    public List<Customer> getAllCustomers(@PathVariable String bankNo) {
        return bankService.getAllCustomers(bankNo);
    }

    @PatchMapping("/deposit/{bankNo}/{customerNo}/{accountNo}/{amount}")
    public ResponseEntity<?> deposit(@PathVariable String accountNo,
                          @PathVariable String bankNo, @PathVariable String customerNo,
                          @PathVariable double amount) {
        try {
            bankService.deposit(bankNo, customerNo, accountNo, amount);
            return new ResponseEntity<>("deposit successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/withdraw/{bankNo}/{customerNo}/{accountNo}/{amount}/{pin}")
    public ResponseEntity<?> withdraw(@PathVariable String accountNo,
                           @PathVariable String bankNo, @PathVariable String customerNo,
                           @PathVariable double amount, @PathVariable String pin) {
        try {
            bankService.withdraw(bankNo, customerNo, accountNo, amount, pin);
            return new ResponseEntity<>("withdraw successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/transfer/{bankNo}/{customerNo}/{senderAccountNo}/{receiverAccountNo}/{amount}/{pin}")
    public ResponseEntity<?> transfer(@PathVariable String bankNo,
                           @PathVariable String customerNo,
                           @PathVariable String senderAccountNo,
                           @PathVariable String receiverAccountNo,
                           @PathVariable double amount, @PathVariable String pin) {
        try {
            bankService.transfer(bankNo, customerNo, senderAccountNo, receiverAccountNo, amount,pin);
            return new ResponseEntity<>("transfer successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAccount/{bankNo}/{customerNo}/{accountNo}")
    public AccountResp getAccount(@PathVariable String accountNo, @PathVariable String bankNo,
                                  @PathVariable String customerNo){
        try {
            return bankService.getAccount(bankNo,customerNo,accountNo);
        }catch (BankException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    @GetMapping("/getBalance/{bankNo}/{customerNo}/{accountNo}")
    public BigDecimal getBalance(@PathVariable String accountNo,
                                 @PathVariable String bankNo, @PathVariable String customerNo){
        return bankService.getBalance(bankNo,customerNo,accountNo);
    }
}