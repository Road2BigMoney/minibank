package dev.zuzu.minibank.console.command;

import dev.zuzu.minibank.console.ConsoleOperationType;
import dev.zuzu.minibank.console.OperationCommand;
import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.model.User;
import dev.zuzu.minibank.service.AccountService;
import dev.zuzu.minibank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateAccountCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public CreateAccountCommand(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        System.out.println("Enter user id:");
        int userId = Integer.parseInt(scanner.nextLine().trim());
        User user = userService.getUserById(userId);
        Account account = accountService.createAccount(userId);
        user.addAccount(account);
        System.out.println("Account created: " + account);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
