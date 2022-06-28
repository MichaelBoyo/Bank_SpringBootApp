package com.example.bank.service;

import com.example.bank.dto.requests.AccountDto;
import com.example.bank.dto.requests.BankRequest;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.bank.models.AccountTypes.CURRENT;
import static com.example.bank.models.AccountTypes.SAVINGS;
import static com.example.bank.models.Gender.FEMALE;
import static com.example.bank.models.Gender.MALE;
import static com.example.bank.models.TransactionType.*;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    BankRepository bankRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionHistoryService txService;

    @Override
    public void createBank(BankRequest bankRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM--dd HH-mm");
        Bank bank = new Bank(bankRequest.getBankName(),generateBankNo(), LocalDateTime.now().format(formatter));
        bankRepository.save(bank);
    }

    private String generateBankNo() {
        Random random = new Random(10);
        StringBuilder bankNo = new StringBuilder();
        bankNo.append(1);
        for (int i = 0; i < 3; i++) {
            bankNo.append(random.nextInt(10));
        }
        return bankNo.toString();
    }


    @Override
    public void addCustomer(String bankNo, CustomerRequest customerRequest) {
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
    }


    @Override
    public void addAccount(String bankNo, AccountDto accountdto, String customerID, String pin) {
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
        } else throw new BankException("customer with " + customerID + " doesn't exist in " + bank.getBankName());
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
    public void deposit(String bankNo, String customerNo,
                        String accountNo, double amount) {

        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (bank.getCustomers().contains(customer)) {
            if (customer.getAccounts().contains(account)) {
                TransactionHistory history = depositHistory(amount);
                TransactionHistory history_ = txService.addTransaction(history);
                accountService.deposit(account, history_);
            } else throw new BankException("account: " + account.getAccountNumber()
                    + " doesnt belong to customer: " + customer.getFirstName());
        } else throw new BankException("customer with " + customerNo + " doesn't exist in " + bank.getBankName());

    }

    private TransactionHistory depositHistory(double amount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        return new TransactionHistory("0", BigDecimal.valueOf(amount),
                DEPOSIT, LocalDateTime.now().format(formatter), "self", "self");
    }


    @Override
    public BankResponse getBank(String bankId) {
        Bank bank = getBankFromDB(bankId);
        return new BankResponse(bank.getBankNo(),bank.getBankName(),
                bank.getCustomers().size(),bank.getDateCreated(),bank.getCustomers());
    }

    @Override
    public void withdraw(String bankNo, String customerNo,
                         String accountNo, double amount, String pin) throws BankException {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (bank.getCustomers().contains(customer)) {
            if (customer.getAccounts().contains(account)) {
                if (accountService.getBalance(account).compareTo(BigDecimal.valueOf(amount)) >= 0) {
                    TransactionHistory history = withdrawHistory(amount);
                    TransactionHistory history_ = txService.addTransaction(history);
                    accountService.withdraw(account, history_, pin);
                } else throw new BankException("inSufficient balance");
            } else
                throw new BankException("account: " + account.getAccountNumber() +
                        " doesnt belong to customer: " + customer.getFirstName());
        } else throw new BankException("customer with " + customerNo + " doesn't exist in " + bank.getBankName());
    }

    private TransactionHistory withdrawHistory(double amount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        return new TransactionHistory("0", BigDecimal.valueOf(amount),
                WITHDRAW, LocalDateTime.now().format(formatter), "self", "self");
    }

    @Override
    public void transfer(String bankNo, String customerNo,
                         String senderAccountNo, String receiverAccountNo,
                         double amount, String senderPin) throws BankException {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account senderAc = accountService.getAccount(senderAccountNo);
        Account receiverAc = accountService.getAccount(receiverAccountNo);
        if (bank.getCustomers().contains(customer)) {
            if (customer.getAccounts().contains(senderAc)) {
                if (accountService.getBalance(senderAc).compareTo(BigDecimal.valueOf(amount)) >= 0) {
                    var txOut = transferOut(amount, senderAc, receiverAc);
                    var txIn = transferIn(amount, senderAc, receiverAc);

                    accountService.withdraw(senderAc, txOut, senderPin);
                    accountService.deposit(receiverAc, txIn);
                } else throw new BankException("inSufficient balance");
            } else throw new BankException("");
        } else throw new BankException("");
    }

    private TransactionHistory transferIn(double amount, Account senderAccount, Account receiverAccount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        return new TransactionHistory("0", BigDecimal.valueOf(amount),
                TRANSFER_IN, LocalDateTime.now().format(formatter), senderAccount.getAccountName(), receiverAccount.getAccountName());
    }

    private TransactionHistory transferOut(double amount, Account senderAccount, Account receiverAccount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        return new TransactionHistory("0", BigDecimal.valueOf(amount),
                TRANSFER_OUT, LocalDateTime.now().format(formatter), senderAccount.getAccountName(), receiverAccount.getAccountName());
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
    public BigDecimal getBalance(String bankNo, String customerNo, String accountNo) {
        Bank Bank = getBankFromDB(bankNo);
        Customer customer = customerService.getCustomer(customerNo);
        Account account = accountService.getAccount(accountNo);
        if (Bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            return accountService.getBalance(account);
        }
        return BigDecimal.valueOf(0);
    }
}
