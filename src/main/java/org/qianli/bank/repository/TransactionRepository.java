package org.qianli.bank.repository;

import org.qianli.bank.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    List<Transaction> findAll();
    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    void deleteById(Long id);
    Transaction update(Transaction transaction);
}
