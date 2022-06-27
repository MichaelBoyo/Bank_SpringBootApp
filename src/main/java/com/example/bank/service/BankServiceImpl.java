package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.Account;
import com.example.bank.models.Bank;
import com.example.bank.models.Customer;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.BankRepository;
import com.example.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.bank.models.AccountTypes.SAVINGS;

@Service
public class BankServiceImpl implements BankService {
    private static int accountNo = 100;
    private static int uid = 0;
    private static int cuid = 0;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;


    @Override
    public String createBank(Bank bank) {
        bank.setBankNo(String.valueOf(++uid));
        bankRepository.save(bank);
        return "created successfully";
    }

    @Override
    public String addCustomer(String bankNo, Customer customer) {
        Bank bank = bankRepository.findBanksByBankNo(bankNo);
        customer.setCustomerNo(String.valueOf(++cuid));
        Customer savedCustomer = customerRepository.save(customer);
        bank.getCustomers().add(savedCustomer);
        bankRepository.save(bank);
        return "added successfully";
    }

    @Override
    public String addAccount(String bankNo, Account account, String customerID, String pin) {
        Bank bank = bankRepository.findBanksByBankNo(bankNo);
        Customer customerToAddAccount = customerRepository.findCustomerByCustomerNo(customerID);
        if (customerToAddAccount != null) {
            List<Customer> customers = bank.getCustomers();
            for (Customer customer : customers) {
                if (customer.getEmail().equals(customerToAddAccount.getEmail())) {
                    account.setAccountType(SAVINGS);
                    account.setPin(pin);
                    account.setAccountNumber(String.valueOf(accountNo++));
                    Account accountToBESaved = accountRepository.save(account);
                    customerToAddAccount.getAccounts().add(accountToBESaved);
                    customerRepository.save(customerToAddAccount);
                    return "added successfully";
                }
            }
        }
        return "failed";
    }

    @Override
    public Customer getCustomer(String bankNo, String customerId) {
        Bank bank = bankRepository.findBanksByBankNo(bankNo);
        List<Customer> customers = bank.getCustomers();
        for (Customer customer : customers) {
            if (Objects.equals(customer.getCustomerNo(), customerId)) {
                return customer;
            }
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
        if (bank.isPresent()) {
            return bank.get();
        }
        throw new BankException("bank with id" + bankNo + " doesn't exist");
    }

    private Customer getCustomerFromDB(String customerNo) {
        Optional<Customer> customer = Optional.ofNullable(customerRepository.findCustomerByCustomerNo(customerNo));
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new BankException("bank with id" + customerNo + " doesn't exist");
    }

    @Override
    public String deposit(String bankNo, String customerNo,
                          String accountNo, double amount) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = getCustomerFromDB(customerNo);
        Account account = getAccountFromDB(accountNo);

        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            accountService.deposit(accountNo, amount);
            return "success";
        }


        return "deposit failed.. check details";
    }

    private Account getAccountFromDB(String accountNo) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findAccountByAccountNumber(accountNo));
        if (account.isPresent()) {
            return account.get();
        }
        throw new BankException("bank with id" + accountNo + " doesn't exist");
    }

    @Override
    public Bank getBank(String bankId) {
        return bankRepository.findBanksByBankNo(bankId);
    }

    @Override
    public String withdraw(String bankNo, String customerNo,
                           String accountNo, double amount) {
        Bank bank = bankRepository.findBanksByBankNo(bankNo);
        for (Customer customer : bank.getCustomers()) {
            if (Objects.equals(customer.getCustomerNo(), customerNo)) {
                for (Account account : customer.getAccounts()) {
                    if (account.getAccountNumber().equals(accountNo)) {
                        accountService.withdraw(accountNo, amount);
                        return "success";
                    }
                }
            }
        }
        return "failed";
    }

    @Override
    public String transfer(String bankNo, String customerNo,
                           String senderAccountNo, String receiverAccountNo,
                           double amount) {
        Bank bank = getBankFromDB(bankNo);
        Customer customer = getCustomerFromDB(customerNo);
        Account senderAccount = getAccountFromDB(senderAccountNo);
        Account receiverAccount = getAccountFromDB(receiverAccountNo);

        if (bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(senderAccount)) {
            performTransfer(senderAccount, receiverAccount, amount);
            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);
            return "transfer successful";
        }


        throw new BankException("transfer failed");
    }

    private void performTransfer(Account senderAccount,
                                 Account receiverAccount, double amount) {

        debit(senderAccount, amount);
        credit(receiverAccount, amount);

    }

    private void credit(Account receiverAccount, double amount) {
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
    }

    private void debit(Account senderAccount, double amount) {
        senderAccount.setBalance(senderAccount.getBalance() - amount);
    }

    @Override
    public Account getAccount(String bankNo, String customerID, String accountID) {
        Bank Bank = getBankFromDB(bankNo);
        Customer customer = getCustomerFromDB(customerID);
        Account account = getAccountFromDB(accountID);
        if (Bank.getCustomers().contains(customer)
                && customer.getAccounts().contains(account)) {
            return account;
        }
        throw new BankException("account not found");
    }
}
