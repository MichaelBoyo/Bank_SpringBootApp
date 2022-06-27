package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionHistoryService txService;
    private static int uid = 0;

    @Override
    public Account addAccount(Account account) {
        account.setAccountNumber(String.valueOf(++uid));
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        BigDecimal currentBalance = new BigDecimal(0);
        final BigDecimal[] balance = {currentBalance};
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        account.getTransactionHistory().forEach(tx -> {
            switch (tx.getType()) {
                case DEPOSIT, TRANSFER_IN -> balance[0] = balance[0].add(tx.getAmount());
                case WITHDRAW, TRANSFER_OUT-> balance[0] = balance[0].subtract(tx.getAmount());
            }
        });
        currentBalance = balance[0];
        return currentBalance;
    }

    @Override
    public String deposit(Account account,TransactionHistory history) {
        TransactionHistory h = txService.addTransaction(history);
        account.getTransactionHistory().add(h);
        accountRepository.save(account);
        return "deposit successful";
    }

    @Override
    public String withdraw(String accountNumber, double amount, TransactionHistory history) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        account.getTransactionHistory().add(history);

        accountRepository.save(account);
        return "withdraw success";
    }

    @Override
    public Account getAccount(String accountID) {
        Optional<Account> account = Optional.ofNullable(accountRepository.findAccountByAccountNumber(accountID));
        return account.orElseThrow(()->{
            throw new BankException("Account not found");
        });
    }

}
