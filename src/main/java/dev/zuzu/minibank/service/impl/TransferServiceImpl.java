package dev.zuzu.minibank.service.impl;

import dev.zuzu.minibank.config.AccountProperties;
import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.service.AccountService;
import dev.zuzu.minibank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {
    private final AccountService accountService;
    private final AccountProperties accountProperties;

    @Autowired
    public TransferServiceImpl(AccountService accountService, AccountProperties accountProperties) {
        this.accountService = accountService;
        this.accountProperties = accountProperties;
    }

    @Override
    public void transfer(int sourceAccountId, int targetAccountId, int amount) {
        accountService.transfer(sourceAccountId, targetAccountId, amount);
    }

    @Override
    public int calculateCommission(int amount) {
        return (int) (amount * accountProperties.getTransferCommission());
    }

    @Override
    public boolean isSameUser(int sourceAccountId, int targetAccountId) {
        Account sourceAccount = accountService.getAccountById(sourceAccountId);
        Account targetAccount = accountService.getAccountById(targetAccountId);
        return sourceAccount.getUserId() == targetAccount.getUserId();
    }
}
