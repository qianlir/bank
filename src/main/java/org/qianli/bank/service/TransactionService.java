package org.qianli.bank.service;

import org.qianli.bank.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {
    //ArrayList多线程不安全，使用Concurrent下的list
    private static List<Transaction> transactions =new CopyOnWriteArrayList<>();
    //换成原子类
    private static AtomicLong nextId = new AtomicLong(1);

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(nextId.addAndGet(1));
        transactions.add(transaction);
        return transaction;
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction transaction = getTransactionById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
            
        transaction.setType(updatedTransaction.getType());
        transaction.setAmount(updatedTransaction.getAmount());
        transaction.setDescription(updatedTransaction.getDescription());
        return transaction;
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transactions.remove(transaction);
    }
}
