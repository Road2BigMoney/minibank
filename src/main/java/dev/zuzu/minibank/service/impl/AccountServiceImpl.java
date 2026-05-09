package dev.zuzu.minibank.service.impl;

import dev.zuzu.minibank.config.AccountProperties;
import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final AccountProperties accountProperties;
    private int nextId = 1;

    @Autowired
    public AccountServiceImpl(AccountProperties accountProperties) {
        this.accountProperties = accountProperties;
    }

    @Override
    public Account createAccount(int userId) {
        int accountId = nextId++;
        Account account = new Account(accountId, userId, accountProperties.getDefaultAmount());
        accounts.put(accountId, account);
        return account;
    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account with id " + accountId + " not found");
        }
        return account;
    }

    @Override
    public Account deposit(int accountId, int amount) {
        validateAmount(amount, "Deposit");

        Account account = getAccountById(accountId);
        account.setMoneyAmount(account.getMoneyAmount() + amount);
        return account;
    }

    @Override
    public Account withdraw(int accountId, int amount) {
        validateAmount(amount, "Withdrawal");

        Account account = getAccountById(accountId);
        validateSufficientFunds(account, amount);

        account.setMoneyAmount(account.getMoneyAmount() - amount);
        return account;
    }

    @Override
    public void transfer(int sourceAccountId, int targetAccountId, int amount) {
        validateAmount(amount, "Transfer");

        Account sourceAccount = getAccountById(sourceAccountId);
        Account targetAccount = getAccountById(targetAccountId);

        validateSufficientFunds(sourceAccount, amount);

        performTransfer(sourceAccount, targetAccount, amount);
    }

    @Override
    public void closeAccount(int accountId) {
        Account account = getAccountById(accountId);
        accounts.remove(account.getId());
    }

    private void validateAmount(int amount, String operation) {
        if (amount <= 0) {
            throw new IllegalArgumentException(operation + " amount must be positive");
        }
    }

    private void validateSufficientFunds(Account account, int amount) {
        if (account.getMoneyAmount() < amount) {
            throw new IllegalArgumentException(
                    String.format("Insufficient funds on account id=%d, balance=%d, attempted=%d",
                            account.getId(), account.getMoneyAmount(), amount)
            );
        }
    }

    private void performTransfer(Account sourceAccount, Account targetAccount, int amount) {
        sourceAccount.setMoneyAmount(sourceAccount.getMoneyAmount() - amount);

        if (sourceAccount.getUserId() == targetAccount.getUserId()) {
            targetAccount.setMoneyAmount(targetAccount.getMoneyAmount() + amount);
        } else {
            int receivedAmount = amount - calculateCommission(amount);
            targetAccount.setMoneyAmount(targetAccount.getMoneyAmount() + receivedAmount);
        }
    }

    private int calculateCommission(int amount) {
        return (int) (amount * accountProperties.getTransferCommission());
    }
}
