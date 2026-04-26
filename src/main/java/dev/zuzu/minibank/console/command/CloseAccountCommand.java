package dev.zuzu.minibank.console.command;

import dev.zuzu.minibank.console.ConsoleOperationType;
import dev.zuzu.minibank.console.OperationCommand;
import dev.zuzu.minibank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CloseAccountCommand implements OperationCommand {
    private final UserService userService;
    private final Scanner scanner;

    @Autowired
    public CloseAccountCommand(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(scanner.nextLine().trim());
        userService.closeAccount(accountId);
        System.out.println("Account " + accountId + " closed successfully");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
