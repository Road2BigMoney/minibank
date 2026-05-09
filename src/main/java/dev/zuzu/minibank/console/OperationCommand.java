package dev.zuzu.minibank.console;

public interface OperationCommand {
    void execute();
    ConsoleOperationType getOperationType();
}
