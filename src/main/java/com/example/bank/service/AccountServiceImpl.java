package com.example.bank.service;

import com.example.bank.exceptions.BankException;
import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import static com.example.bank.models.AccountTypes.SAVINGS;

@Service
public class AccountServiceImpl implements AccountService {
    private static int uid = 0;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public Account addAccount(Account account, String pin) {
        account.setPin(pin);
        account.setAccountType(SAVINGS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM--dd HH-mm");
        account.setDateCreated(LocalDateTime.now().format(formatter));
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal getBalance(Account account) {
        BigDecimal currentBalance = new BigDecimal(0);
        final BigDecimal[] balance = {currentBalance};
        account.getTransactionHistory().forEach(tx -> {
            switch (tx.getType()) {
                case DEPOSIT, TRANSFER_IN -> balance[0] = balance[0].add(tx.getAmount());
                case WITHDRAW, TRANSFER_OUT -> balance[0] = balance[0].subtract(tx.getAmount());
            }
        });
        currentBalance = balance[0];
        return currentBalance;
    }

    @Override
    public String deposit(Account account, TransactionHistory history) {
        history = transactionHistoryRepository.save(history);
        account.getTransactionHistory().add(history);
        accountRepository.save(account);
        return "deposit successful";
    }

    @Override
    public String withdraw(Account account, TransactionHistory history, String pin) {
        if (account.pinIsValid(pin)) {
            history = transactionHistoryRepository.save(history);
            account.getTransactionHistory().add(history);
            accountRepository.save(account);
            return "successful";
        }
        return "failed";
    }


    @Override
    public Account getAccount(String accountID) {
        Optional<Account> account = accountRepository.findById(accountID);
        return account.orElseThrow(() -> {
            throw new BankException("Account not found");
        });
    }
    private String generateAccountNo(){
        Random random = new Random(10);
        StringBuilder accountNo = new StringBuilder();
        accountNo.append(102);
        for (int i = 0; i <7 ; i++) {
            accountNo.append(random.nextInt(10));
        }
        return accountNo.toString();
    }

}
