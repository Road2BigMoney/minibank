package dev.zuzu.minibank.service;

public interface TransferService {
    void transfer(int sourceAccountId, int targetAccountId, int amount);
    int calculateCommission(int amount);
    boolean isSameUser(int sourceAccountId, int targetAccountId);
}
