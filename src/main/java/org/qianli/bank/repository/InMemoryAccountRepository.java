package org.qianli.bank.repository;

import org.springframework.stereotype.Repository;
import org.qianli.bank.model.Account;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public InMemoryAccountRepository() {
        // 初始化10个模拟账户
        for (int i = 1; i <= 10; i++) {
            String accountNumber = String.format("A%03d", i); // A001-A010
            String accountName = "账户" + i;
            BigDecimal balance = new BigDecimal("10000.00");
            Account account = new Account((long)i,accountNumber, accountName, balance);
            accounts.put(accountNumber, account);
        }
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public Account save(Account account) {
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    @Override
    public void deleteByAccountNumber(String accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public Account update(Account account) {
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    @Override
    public boolean deposit(String accountNumber, BigDecimal amount) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.setBalance(account.getBalance().add(amount));
            return true;
        }
        return false;
    }

    @Override
    public boolean withdraw(String accountNumber, BigDecimal amount) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            return true;
        }
        return false;
    }
}
