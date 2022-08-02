package com.example.bank.dto.response;

import com.example.bank.models.AccountTypes;
import com.example.bank.models.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResp {
    private String accountNo;
    private String accountName;
    private AccountTypes accountType;
    private List<TransactionHistory> transactionHistory;
}
