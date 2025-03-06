package org.qianli.bank.repository;

import org.qianli.bank.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findAll();
    Optional<Account> findByAccountNumber(String accountNumber);
    Account save(Account account);
    void deleteByAccountNumber(String accountNumber);
    Account update(Account account);
    
    // 余额相关操作
    boolean deposit(String accountNumber, BigDecimal amount);
    boolean withdraw(String accountNumber, BigDecimal amount);
}
