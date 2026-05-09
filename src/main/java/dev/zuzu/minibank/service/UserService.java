package dev.zuzu.minibank.service;

import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.model.User;

import java.util.Collection;

public interface UserService {
    User createUser(String login);
    User getUserById(int userId);

    void removeAccountFromUser(int userId, Account account);

    User getUserByAccountId(int accountId);
    Collection<User> getAllUsers();
    void addAccountToUser(int userId, Account account);
    void closeAccount(int accountId);
}
