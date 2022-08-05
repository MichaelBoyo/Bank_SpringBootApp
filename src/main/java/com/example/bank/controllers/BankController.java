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
    private BankService bankService;

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
        }catch(BankException err){
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
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        try {
            bankService.withdraw(withdrawRequest.getBankNo(),
                    withdrawRequest.getCustomerNo(), withdrawRequest.getAccountNo(),
                    withdrawRequest.getAmount(), withdrawRequest.getPin());
            return new ResponseEntity<>(withdrawRequest.getAmount()+" withdraw successful", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest) {
        try {
            bankService.transfer(transferRequest.getBankNo(), transferRequest.getCustomerNo(),
                    transferRequest.getSenderAccountNo(), transferRequest.getReceiverAccountNo(),
                    transferRequest.getAmount(),transferRequest.getPin());
            return new ResponseEntity<>(transferRequest.getAmount()+" transferred successfully;", HttpStatus.OK);
        } catch (BankException err){
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAccount")
    public AccountResp getAccount(@RequestBody GetAccountRequest getAccountRequest){
        try {
            return bankService.getAccount(getAccountRequest.getBankNo(),getAccountRequest.getCustomerNo(),
                    getAccountRequest.getAccountNo());
        }catch (BankException e){
            e.printStackTrace();
            return null;
        }

    }
    @GetMapping("/getBalance")
    public BigDecimal getBalance(@RequestBody GetBalanceRequest getBalanceRequest){
        return bankService.getBalance(getBalanceRequest.getBankNo(),
                getBalanceRequest.getCustomerNo(),getBalanceRequest.getAccountNo());
    }
}