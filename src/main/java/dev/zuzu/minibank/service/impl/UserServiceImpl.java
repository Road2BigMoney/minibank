package dev.zuzu.minibank.service.impl;

import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.model.User;
import dev.zuzu.minibank.service.AccountService;
import dev.zuzu.minibank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private final AccountService accountService;
    private int nextId = 1;

    @Autowired
    public UserServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public User createUser(String login) {
        validateLogin(login);
        checkUserExists(login);

        int userId = nextId++;
        Account initialAccount = accountService.createAccount(userId);
        User user = new User(userId, login, initialAccount);
        users.put(userId, user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addAccountToUser(int userId, Account account) {
        User user = getUserById(userId);
        user.addAccount(account);
    }

    @Override
    public void removeAccountFromUser(int userId, Account account) {
        User user = getUserById(userId);
        user.removeAccount(account);
    }

    @Override
    public User getUserByAccountId(int accountId) {
        for (User user : users.values()) {
            for (Account account : user.getAccounts()) {
                if (account.getId() == accountId) {
                    return user;
                }
            }
        }
        throw new IllegalArgumentException("No user found with account id " + accountId);
    }

    @Override
    public void closeAccount(int accountId) {
        User user = getUserByAccountId(accountId);
        Account accountToClose = findAccountById(user, accountId);

        validateAccountClosure(user, accountToClose);
        transferFundsToRemainingAccount(user, accountToClose);

        removeAccountFromUser(user.getId(), accountToClose);
        accountService.closeAccount(accountId);
    }

    private void validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
    }

    private void checkUserExists(String login) {
        User existed = users.get(login);
        if (existed != null) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists");
        }
    }

    private Account findAccountById(User user, int accountId) {
        for (Account account : user.getAccounts()) {
            if (account.getId() == accountId) {
                return account;
            }
        }
        throw new IllegalArgumentException("Account with id " + accountId + " not found for user " + user.getLogin());
    }

    private void validateAccountClosure(User user, Account accountToClose) {
        if (user.getAccounts().size() == 1) {
            throw new IllegalArgumentException("Cannot close the only account of user " + user.getLogin());
        }
    }

    private void transferFundsToRemainingAccount(User user, Account accountToClose) {
        int remainingAmount = accountToClose.getMoneyAmount();

        for (Account account : user.getAccounts()) {
            if (account.getId() != accountToClose.getId()) {
                account.setMoneyAmount(account.getMoneyAmount() + remainingAmount);
                break;
            }
        }
    }
}
