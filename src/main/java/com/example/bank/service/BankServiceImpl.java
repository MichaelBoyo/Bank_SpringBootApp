package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.*;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.BankRepository;
import com.example.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.bank.models.TransactionType.*;

@Service
public class BankServiceImpl implements BankService {
    private static int uid = 0;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    CustomerService customerService;
    @Autowired
    TransactionHistoryService txService;

    @Override
    public String createBank(Bank bank) {
        bank.setBankNo(String.valueOf(++uid));
        bankRepository.save(bank);
        return "created successfully";
    }

    @Override
    public String addCustomer(String bankNo, Customer customer) {
        Bank bank = getBankFromDB(bankNo);
        Customer savedCustomer = customerService.addCustomer(customer);
        bank.getCustomers().add(savedCustomer);
        bankRepository.save(bank);
        return "added successfully";
    }

    @Override
    public String addAccount(String bankNo, Account account, String customerID, String pin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customerToAddAccount = customerService.getCustomer(customerID);
        if(bank.getCustomers().contains(customerToAddAccount)){
            Account account1 = accountService.addAccount(account,pin);
            customerToAddAccount.getAccounts().add(account1);
            customerService.updateCustomer(customerToAddAccount);
            return "success";
        }
        return "failed";
    }

    @Override
    public Customer getCustomer(String bankNo, String customerId) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerId);
        if(bank.getCustomers().contains(customer)){
            return customer;
        }
        throw new IllegalArgumentException("customer not found");
    }

    @Override
    public List<Customer> getAllCustomers(String bankNo) {
        Bank bank = getBankFromDB(bankNo);
        if (bank.getCustomers().size() > 0) {
            return bank.getCustomers();
        }
        throw new BankException("error");
    }

    private Bank getBankFromDB(String bankNo) {
        Optional<Bank> bank = Optional.ofNullable(bankRepository.findBanksByBankNo(bankNo));
        return bank.orElseThrow(()->{
            throw new BankException("bank not found");
        });
    }

    @Override
    public String deposit(String bankNo, String customerNo,
                          String accountNo, double amount) {

        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            TransactionHistory history = depositHistory(amount);
            TransactionHistory history_ = txService.addTransaction(history);
            return accountService.deposit(account,history_);

        }
        return "deposit failed.. check details";
    }
    private static int  transactionNo = 0;

    private TransactionHistory depositHistory(double amount) {
       return new TransactionHistory(String.valueOf(++transactionNo),BigDecimal.valueOf(amount),
                DEPOSIT,LocalDateTime.now(),"self","self" );
    }


    @Override
    public Bank getBank(String bankId) {
        Optional<Bank> bank = Optional.ofNullable(bankRepository.findBanksByBankNo(bankId));
        return bank.orElseThrow(()->{
            throw new BankException("Bank with id "+bankId+" not found");
        });
    }

    @Override
    public String withdraw(String bankNo, String customerNo,
                           String accountNo, double amount, String pin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            TransactionHistory history = withdrawHistory(amount);
            TransactionHistory history_ = txService.addTransaction(history);
            return accountService.withdraw(account,history_,pin);

        }
        return "deposit failed.. check details";
    }

    private TransactionHistory withdrawHistory(double amount) {
        return new TransactionHistory(String.valueOf(++transactionNo),BigDecimal.valueOf(amount),
                WITHDRAW,LocalDateTime.now(),"self","self" );
    }

    @Override
    public String transfer(String bankNo, String customerNo,
                           String senderAccountNo, String receiverAccountNo,
                           double amount, String pin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account senderAccount = accountService.getAccount(senderAccountNo);
        Account receiverAccount = accountService.getAccount(receiverAccountNo);
        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(senderAccount)) {

            TransactionHistory transferOutHistory = transferOut(amount,senderAccount,receiverAccount);
            TransactionHistory transferInHistory = transferIn(amount,senderAccount,receiverAccount);
            accountService.withdraw(senderAccount,transferOutHistory,pin);
            accountService.deposit(receiverAccount,transferInHistory);
            return "transfer successful";
        }

        return "transfer failed";
    }

    private TransactionHistory transferIn(double amount, Account senderAccount, Account receiverAccount) {
        return new TransactionHistory(String.valueOf(++transactionNo),BigDecimal.valueOf(amount),
                TRANSFER_IN,LocalDateTime.now(),senderAccount.getAccountName(),receiverAccount.getAccountName() );
    }

    private TransactionHistory transferOut(double amount, Account senderAccount, Account receiverAccount) {
        return new TransactionHistory(String.valueOf(++transactionNo),BigDecimal.valueOf(amount),
                TRANSFER_OUT,LocalDateTime.now(),senderAccount.getAccountName(),receiverAccount.getAccountName() );
    }



    @Override
    public Account getAccount(String bankNo, String customerID, String accountID) {
        Bank Bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerID);
        Account account = accountService.getAccount(accountID);
        if (Bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            return account;
        }
        throw new BankException("account not found");
    }

    @Override
    public String getBalance(String bankNo, String customerNo, String accountNo) {
        Bank Bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (Bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            return String.valueOf(accountService.getBalance(account));
        }
        return "zero";
    }
}
