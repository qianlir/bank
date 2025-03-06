package org.qianli.bank.service;

import org.qianli.bank.model.Transaction;
import org.qianli.bank.model.Account;
import org.qianli.bank.repository.TransactionRepository;
import org.qianli.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
   

    public List<Transaction> getAllTransactions(int page, int size, String type, String accountId) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be > 0");
        }
        return transactionRepository.findAll(page, size, type, accountId);
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        validateTransaction(transaction);
        
        try {
            transaction.setTimestamp(java.time.LocalDateTime.now());
            
            switch (transaction.getType()) {
                case DEPOSIT:
                    handleDeposit(transaction);
                    break;
                case WITHDRAWAL:
                    handleWithdrawal(transaction);
                    break;
                case TRANSFER:
                    handleTransfer(transaction);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid transaction type");
            }
            
            return transactionRepository.save(transaction);
            
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }

    private void handleDeposit(Transaction transaction) {
        if (transaction.getToAccountNumber() == null || transaction.getToAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("To account is required for deposit");
        }
        
        Account account = accountRepository.findByAccountNumber(transaction.getToAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        accountRepository.deposit(transaction.getToAccountNumber(), transaction.getAmount());
    }

    private void handleWithdrawal(Transaction transaction) {
        if (transaction.getFromAccountNumber() == null || transaction.getFromAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("From account is required for withdrawal");
        }
        
        Account account = accountRepository.findByAccountNumber(transaction.getFromAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        
        accountRepository.withdraw(transaction.getFromAccountNumber(), transaction.getAmount());
    }

    private void handleTransfer(Transaction transaction) {
        if (transaction.getFromAccountNumber() == null || transaction.getFromAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("From account is required for transfer");
        }
        if (transaction.getToAccountNumber() == null || transaction.getToAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("To account is required for transfer");
        }
        if (transaction.getFromAccountNumber().equals(transaction.getToAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        acquireLocks(transaction.getFromAccountNumber(), transaction.getToAccountNumber());
        try {
            Account fromAccount = accountRepository.findByAccountNumber(transaction.getFromAccountNumber())
                    .orElseThrow(() -> new IllegalArgumentException("From account not found"));
            Account toAccount = accountRepository.findByAccountNumber(transaction.getToAccountNumber())
                    .orElseThrow(() -> new IllegalArgumentException("To account not found"));

            if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }

            accountRepository.withdraw(transaction.getFromAccountNumber(), transaction.getAmount());
            accountRepository.deposit(transaction.getToAccountNumber(), transaction.getAmount());
        } finally {
            releaseLocks(transaction.getFromAccountNumber(), transaction.getToAccountNumber());
        }
    }
   // 使用ConcurrentHashMap存储每个账户对应的锁
    private static final ConcurrentHashMap<String, Lock> accountLocks = new ConcurrentHashMap<>();

    private void acquireLocks(String fromAccount, String toAccount) {
        // 确定锁的获取顺序
        String firstAccount = fromAccount.compareTo(toAccount) < 0 ? fromAccount : toAccount;
        String secondAccount = fromAccount.compareTo(toAccount) < 0 ? toAccount : fromAccount;

        // 获取或创建锁对象（线程安全）
        Lock firstLock = accountLocks.computeIfAbsent(firstAccount, k -> new ReentrantLock());
        Lock secondLock = accountLocks.computeIfAbsent(secondAccount, k -> new ReentrantLock());

        // 按顺序获取锁
        firstLock.lock();
        try {
            secondLock.lock();
        } catch (Exception e) {
            // 如果获取第二个锁失败，释放第一个锁
            firstLock.unlock();
            throw new RuntimeException("Failed to acquire locks", e);
        }
    }

    private void releaseLocks(String fromAccount, String toAccount) {
        // 确定锁的释放顺序（必须与获取顺序相反）
        String firstAccount = fromAccount.compareTo(toAccount) < 0 ? fromAccount : toAccount;
        String secondAccount = fromAccount.compareTo(toAccount) < 0 ? toAccount : fromAccount;

        Lock secondLock = accountLocks.get(secondAccount);
        Lock firstLock = accountLocks.get(firstAccount);

        if (secondLock != null) {
            secondLock.unlock();
        }
        if (firstLock != null) {
            firstLock.unlock();
        }
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        updatedTransaction.setId(id);
        return transactionRepository.update(updatedTransaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
