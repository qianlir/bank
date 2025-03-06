package org.qianli.bank.repository;

import org.qianli.bank.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    // 使用CopyOnWriteArrayList保证线程安全
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();
    // 使用AtomicLong生成唯一ID
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<Transaction> findAll(int page, int size, String type, String accountId) {
        //暂时每次拷贝下所有数组，后续优化
        List<Transaction> result = new ArrayList<>(transactions);
        
        // Apply filters
        if (type != null && !type.isEmpty()) {
            result = result.stream()
                .filter(t -> t.getType().toString().equalsIgnoreCase(type))
                .toList();
        }
        
        if (accountId != null && !accountId.isEmpty()) {
            result = result.stream()
                .filter(t -> accountId.equals(t.getFromAccountNumber()) || accountId.equals(t.getToAccountNumber()))
                .toList();
        }
        
        // Apply pagination
        int start = page * size;
        if (start >= result.size()) {
            return List.of();
        }
        
        int end = Math.min(start + size, result.size());
        return result.subList(start, end);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public Transaction save(Transaction transaction) {
        transaction.setId(nextId.getAndIncrement());
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public void deleteById(Long id) {
        transactions.removeIf(t -> t.getId().equals(id));
    }

    @Override
    public Transaction update(Transaction transaction) {
        Transaction existing = findById(transaction.getId())
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transaction.getId()));
        
        existing.setType(transaction.getType());
        existing.setAmount(transaction.getAmount());
        existing.setDescription(transaction.getDescription());
        return existing;
    }

    @Override
    public int getTransactionCount() {
        return this.transactions.size();
    }
}
