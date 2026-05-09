package dev.zuzu.minibank.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleListener {
    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final Scanner scanner;

    @Autowired
    public ConsoleListener(List<OperationCommand> commands) {
        this.commandMap = new HashMap<>();
        commands.forEach(command ->
            commandMap.put(command.getOperationType(), command)
        );
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcomeMessage();

        while (true) {
            System.out.println("\nEnter command:");
            String command = scanner.nextLine().trim();

            if (command.equals("EXIT")) {
                break;
            }

            try {
                processCommand(command);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printWelcomeMessage() {
        System.out.println("MiniBank started. Type EXIT to stop.");
        System.out.println("Available commands: USER_CREATE, SHOW_ALL_USERS, ACCOUNT_CREATE, " +
                "ACCOUNT_DEPOSIT, ACCOUNT_WITHDRAW, ACCOUNT_TRANSFER, ACCOUNT_CLOSE, EXIT");
    }

    private void processCommand(String command) {
        try {
            ConsoleOperationType operationType = ConsoleOperationType.valueOf(command);
            OperationCommand operationCommand = commandMap.get(operationType);

            if (operationCommand != null) {
                operationCommand.execute();
            } else {
                System.out.println("No handler found for command: " + command);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
