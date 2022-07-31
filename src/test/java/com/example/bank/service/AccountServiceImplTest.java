package com.example.bank.service;

import com.example.bank.models.Account;
import com.example.bank.models.AccountTypes;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static com.example.bank.models.TransactionType.DEPOSIT;
import static com.example.bank.models.TransactionType.WITHDRAW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void addAccount() {
        var account = new Account();
        accountService.addAccount(account, "1234");
        var accountArgCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgCaptor.capture());
        var savedAccount = accountArgCaptor.getValue();

        assertNotNull(savedAccount.getAccountNumber());
        assertEquals("1234", savedAccount.getPin());
        assertNotNull(savedAccount.getDateCreated());
        assertEquals(AccountTypes.SAVINGS, savedAccount.getAccountType());
    }

    @Test
    void getBalance() {
        var account = new Account();
        TransactionHistory history = new TransactionHistory();
        history.setType(DEPOSIT);
        history.setAmount(BigDecimal.valueOf(1000));
        when(transactionHistoryRepository
                .save(any(TransactionHistory.class)))
                .thenReturn(history);
        accountService.deposit(account, history);
        var accountArgCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgCaptor.capture());
        var savedAccount = accountArgCaptor.getValue();
        assertEquals(BigDecimal.valueOf(1000), accountService.getBalance(savedAccount));
        assertEquals(history.getType(),DEPOSIT);
    }

    @Test
    void deposit() {
        var account = new Account();
        var history = new TransactionHistory();
        when(transactionHistoryRepository
                .save(any(TransactionHistory.class)))
                .thenReturn(history);

        accountService.deposit(account, history);
        var accountArgCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgCaptor.capture());
        var savedAccount = accountArgCaptor.getValue();
        assertEquals(1, savedAccount.getTransactionHistory().size());
        assertEquals(history, savedAccount.getTransactionHistory().get(0));
    }

    @Test
    void withdraw() {
        var account = new Account();
        account.setPin("1234");
        TransactionHistory history = new TransactionHistory();
        history.setType(DEPOSIT);
        history.setAmount(BigDecimal.valueOf(1200));
        when(transactionHistoryRepository
                .save(any(TransactionHistory.class)))
                .thenReturn(history);
        accountService.deposit(account, history);

        var accountArgCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgCaptor.capture());
        var savedAccount = accountArgCaptor.getValue();

        var txWithdraw = new TransactionHistory();
        txWithdraw.setType(WITHDRAW);
        txWithdraw.setAmount(BigDecimal.valueOf(1000));
        when(transactionHistoryRepository
                .save(any(TransactionHistory.class)))
                .thenReturn(txWithdraw);
        accountService.withdraw(savedAccount,txWithdraw, "1234");

        assertEquals(BigDecimal.valueOf(200), accountService.getBalance(savedAccount));
        assertEquals(history.getType(),DEPOSIT);
        assertEquals(txWithdraw.getType(),WITHDRAW);
    }

    @Test
    void getAccount() {
        var account = new Account();
        account.setAccountName("mike");
        accountService.addAccount(account, "1234");
        var acNo= account.getAccountNumber();
        var accountArgCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgCaptor.capture());
        var savedAccount = accountArgCaptor.getValue();

        assertEquals(acNo, savedAccount.getAccountNumber());
        assertEquals("mike", savedAccount.getAccountName());
    }
}