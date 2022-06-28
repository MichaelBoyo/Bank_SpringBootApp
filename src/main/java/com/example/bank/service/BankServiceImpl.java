package com.example.bank.service;

import com.example.bank.dto.requests.AccountDto;
import com.example.bank.dto.requests.CustomerRequest;
import com.example.bank.dto.response.AccountResp;
import com.example.bank.dto.response.BankResponse;
import com.example.bank.dto.response.CustomerResponse;
import com.example.bank.exceptions.BankException;
import com.example.bank.models.*;
import com.example.bank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static com.example.bank.models.AccountTypes.CURRENT;
import static com.example.bank.models.AccountTypes.SAVINGS;
import static com.example.bank.models.Gender.FEMALE;
import static com.example.bank.models.Gender.MALE;
import static com.example.bank.models.TransactionType.*;

@Service
public class BankServiceImpl implements BankService {
    private static int uid = 0;
    private static int transactionNo = 0;
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
    public String addCustomer(String bankNo, CustomerRequest customerRequest) {
        Bank bank = getBankFromDB(bankNo);
        LocalDate dob = LocalDate.of(customerRequest.getYear(),
                customerRequest.getMonth(), customerRequest.getDay());
        Gender gender = MALE;
        if (customerRequest.getGender().equalsIgnoreCase("female")) {
            gender = FEMALE;
        }
        Customer customer = new Customer(dob, customerRequest.getFirstName(), customerRequest.getLastName(),
                gender, customerRequest.getEmail(), customerRequest.getPhoneNumber());
        Customer savedCustomer = customerService.addCustomer(customer);
        bank.getCustomers().add(savedCustomer);
        bankRepository.save(bank);
        return "added successfully";
    }

    @Override
    public String addAccount(String bankNo, AccountDto accountdto, String customerID, String pin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customerToAddAccount = customerService.getCustomer(customerID);
        if (bank.getCustomers().contains(customerToAddAccount)) {
            AccountTypes type = SAVINGS;
            if (accountdto.getAccountType().equalsIgnoreCase("current")) {
                type = CURRENT;
            }
            Account account = new Account(accountdto.getAccountName(), type);
            Account account1 = accountService.addAccount(account, pin);
            customerToAddAccount.getAccounts().add(account1);
            customerService.updateCustomer(customerToAddAccount);
            return "success";
        }
        return "failed";
    }

    @Override
    public CustomerResponse getCustomer(String bankNo, String customerId) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerId);
        int age = Period.between(customer.getDob(), LocalDate.now()).getYears();
        if (bank.getCustomers().contains(customer)) {
            return new CustomerResponse(customer.getFirstName() + " " + customer.getLastName(),
                    String.valueOf(age), customer.getGender(), customer.getEmail(), customer.getPhoneNumber(), customer.getAccounts());
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
        return bank.orElseThrow(() -> {
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
            return accountService.deposit(account, history_);

        }
        return "deposit failed.. check details";
    }

    private TransactionHistory depositHistory(double amount) {
        return new TransactionHistory(String.valueOf(++transactionNo), BigDecimal.valueOf(amount),
                DEPOSIT, LocalDateTime.now(), "self", "self");
    }


    @Override
    public BankResponse getBank(String bankId) {
        Optional<Bank> bank = Optional.ofNullable(bankRepository.findBanksByBankNo(bankId));
        if (bank.isPresent()) {
            return new BankResponse(bank.get().getBankNo(), bank.get().getBankName(), bank.get().getCustomers().size(), bank.get().getCustomers());
        }
        throw new BankException("Bank with id " + bankId + " not found");
    }

    @Override
    public String withdraw(String bankNo, String customerNo,
                           String accountNo, double amount, String pin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            if (accountService.getBalance(account).compareTo(BigDecimal.valueOf(amount)) >= 0) {
                TransactionHistory history = withdrawHistory(amount);
                TransactionHistory history_ = txService.addTransaction(history);
                return accountService.withdraw(account, history_, pin);

            }
        }
        return "deposit failed.. check details";
    }

    private TransactionHistory withdrawHistory(double amount) {
        return new TransactionHistory(String.valueOf(++transactionNo), BigDecimal.valueOf(amount),
                WITHDRAW, LocalDateTime.now(), "self", "self");
    }

    @Override
    public String transfer(String bankNo, String customerNo,
                           String senderAccountNo, String receiverAccountNo,
                           double amount, String senderPin) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account senderAccount = accountService.getAccount(senderAccountNo);
        Account receiverAccount = accountService.getAccount(receiverAccountNo);
        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(senderAccount)) {
           if(accountService.getBalance(senderAccount).compareTo(BigDecimal.valueOf(amount) )>=0){
               var transferOutHistory = transferOut(amount, senderAccount, receiverAccount);
               var transferInHistory = transferIn(amount, senderAccount, receiverAccount);

               accountService.withdraw(senderAccount, transferOutHistory, senderPin);
               accountService.deposit(receiverAccount, transferInHistory);
               return "transfer successful";
           }


        }

        return "transfer failed";
    }

    private TransactionHistory transferIn(double amount, Account senderAccount, Account receiverAccount) {
        return new TransactionHistory(String.valueOf(++transactionNo), BigDecimal.valueOf(amount),
                TRANSFER_IN, LocalDateTime.now(), senderAccount.getAccountName(), receiverAccount.getAccountName());
    }

    private TransactionHistory transferOut(double amount, Account senderAccount, Account receiverAccount) {
        return new TransactionHistory(String.valueOf(++transactionNo), BigDecimal.valueOf(amount),
                TRANSFER_OUT, LocalDateTime.now(), senderAccount.getAccountName(), receiverAccount.getAccountName());
    }


    @Override
    public AccountResp getAccount(String bankNo, String customerID, String accountID) {
        Bank Bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerID);
        Account account = accountService.getAccount(accountID);
        if (Bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            return new AccountResp(account.getAccountNumber(), account.getAccountName(),
                    account.getAccountType(), account.getTransactionHistory());
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
