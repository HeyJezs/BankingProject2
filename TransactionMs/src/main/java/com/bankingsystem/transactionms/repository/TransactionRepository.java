package com.bankingsystem.transactionms.repository;

import com.bankingsystem.transactionms.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByCuentaOrigenId(String cuentaOrigenId);
}