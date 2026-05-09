package dev.zuzu.minibank.service;

import dev.zuzu.minibank.model.Account;

public interface AccountService {
    Account createAccount(int userId);
    Account getAccountById(int accountId);
    Account deposit(int accountId, int amount);
    Account withdraw(int accountId, int amount);
    void transfer(int sourceAccountId, int targetAccountId, int amount);
    void closeAccount(int accountId);
}
