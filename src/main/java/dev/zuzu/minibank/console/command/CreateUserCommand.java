package dev.zuzu.minibank.console.command;

import dev.zuzu.minibank.console.ConsoleOperationType;
import dev.zuzu.minibank.console.OperationCommand;
import dev.zuzu.minibank.model.User;
import dev.zuzu.minibank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CreateUserCommand implements OperationCommand {
    private final UserService userService;
    private final Scanner scanner;

    @Autowired
    public CreateUserCommand(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        System.out.println("Enter login:");
        String login = scanner.nextLine().trim();
        User user = userService.createUser(login);
        System.out.println("User created: " + user);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
