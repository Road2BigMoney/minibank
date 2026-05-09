package dev.zuzu.minibank.console.command;

import dev.zuzu.minibank.console.ConsoleOperationType;
import dev.zuzu.minibank.console.OperationCommand;
import dev.zuzu.minibank.model.Account;
import dev.zuzu.minibank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class DepositCommand implements OperationCommand {
    private final AccountService accountService;
    private final Scanner scanner;

    @Autowired
    public DepositCommand(AccountService accountService) {
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter amount:");
        int amount = Integer.parseInt(scanner.nextLine().trim());
        Account account = accountService.deposit(accountId, amount);
        System.out.println("Deposited " + amount + " to account " + accountId + ". New balance: " + account.getMoneyAmount());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
