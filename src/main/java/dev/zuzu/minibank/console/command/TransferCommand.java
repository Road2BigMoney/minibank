package dev.zuzu.minibank.console.command;

import dev.zuzu.minibank.console.ConsoleOperationType;
import dev.zuzu.minibank.console.OperationCommand;
import dev.zuzu.minibank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TransferCommand implements OperationCommand {
    private final TransferService transferService;
    private final Scanner scanner;

    @Autowired
    public TransferCommand(TransferService transferService) {
        this.transferService = transferService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        System.out.println("Enter source account id:");
        int sourceAccountId = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter target account id:");
        int targetAccountId = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Enter amount:");
        int amount = Integer.parseInt(scanner.nextLine().trim());

        boolean sameUser = transferService.isSameUser(sourceAccountId, targetAccountId);
        transferService.transfer(sourceAccountId, targetAccountId, amount);

        printTransferResult(sourceAccountId, targetAccountId, amount, sameUser);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }

    private void printTransferResult(int sourceAccountId, int targetAccountId, int amount, boolean sameUser) {
        if (sameUser) {
            System.out.println("Transfer completed from account " + sourceAccountId + " to account " + targetAccountId +
                    ". Amount: " + amount + " (no commission, same user)");
        } else {
            int commission = transferService.calculateCommission(amount);
            int receivedAmount = amount - commission;
            System.out.println("Transfer completed from account " + sourceAccountId + " to account " + targetAccountId +
                    ". Amount: " + amount + ", commission: " + commission +
                    ", recipient received: " + receivedAmount);
        }
    }
}
