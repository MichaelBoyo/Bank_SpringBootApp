package com.example.bank.controllers;

import com.example.bank.dto.requests.*;
import com.example.bank.dto.response.AccountResp;
import com.example.bank.dto.response.BankResponse;
import com.example.bank.dto.response.CustomerResponse;
import com.example.bank.exceptions.BankException;
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
            var bankCreated = bankService.createBank(bank);
            return new ResponseEntity<>(bank.getBankName()+"" +
                    " bank created successfully with id "+bankCreated.getBankId(), HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus. BAD_REQUEST);
        }
    }

    @GetMapping("/viewBank")
    public BankResponse getBank(@RequestParam String bankId) {
        return bankService.getBank(bankId);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerRequest customer) {
        try {
            bankService.addCustomer(customer.getBankNo(), customer);
            return new ResponseEntity<>("customer added successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addAccount")
    public ResponseEntity<?> addAccount(@RequestBody AccountDto accountDto) {

        try {
            bankService.addAccount(accountDto.getBankNo(),
                    accountDto, accountDto.getCustomerId(), accountDto.getPin());

            return new ResponseEntity<>("account added successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCustomer")
    public CustomerResponse getCustomer(@RequestBody GetCustomerDto getCustomerDto) {
        System.out.println(getCustomerDto.getCustomerId() +" and "+ getCustomerDto.getBankNo());
        return bankService.getCustomer(getCustomerDto.getBankNo(), getCustomerDto.getCustomerId());
    }

    @GetMapping("/getAllCustomers")
    public List<Customer> getAllCustomers(@RequestParam String bankNo) {
        return bankService.getAllCustomers(bankNo);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
        try {
            bankService.deposit(depositRequest.getBankNo(), depositRequest.getCustomerNo(),
                    depositRequest.getAccountNo(), depositRequest.getAmount());
            return new ResponseEntity<>(depositRequest.getAmount()+" deposited successfully", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam  String accountNo,
                           @RequestParam  String bankNo, @RequestParam  String customerNo,
                           @RequestParam  Double amount, @RequestParam  String pin) {
        try {
            bankService.withdraw(bankNo, customerNo, accountNo, amount, pin);
            return new ResponseEntity<>(amount+" withdraw successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam  String bankNo,
                           @RequestParam  String customerNo,
                           @RequestParam  String senderAccountNo,
                           @RequestParam  String receiverAccountNo,
                           @RequestParam  Double amount, @RequestParam  String pin) {
        try {
            bankService.transfer(bankNo, customerNo, senderAccountNo, receiverAccountNo, amount,pin);
            return new ResponseEntity<>("transfer successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAccount")
    public AccountResp getAccount(@RequestParam  String accountNo, @RequestParam  String bankNo,
                                  @RequestParam  String customerNo){
        try {
            return bankService.getAccount(bankNo,customerNo,accountNo);
        }catch (BankException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    @GetMapping("/getBalance")
    public BigDecimal getBalance(@RequestParam  String accountNo,
                                 @RequestParam  String bankNo, @RequestParam  String customerNo){
        return bankService.getBalance(bankNo,customerNo,accountNo);
    }
}