package com.bankingsystem.transactionms.service;

import com.bankingsystem.transactionms.client.AccountService;
import com.bankingsystem.transactionms.exception.ResourceNotFoundException;
import com.bankingsystem.transactionms.model.Transaction;
import com.bankingsystem.transactionms.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public Mono<Transaction> registrarDeposito(Transaction transaction) {
        // Validar que el tipo sea DEPOSITO
        if (!"DEPOSITO".equalsIgnoreCase(transaction.getTipo())) {
            return Mono.error(new IllegalArgumentException("El tipo de transacción debe ser DEPOSITO."));
        }

        // Validar datos de entrada
        if (transaction.getMonto() == null || transaction.getMonto() <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a cero."));
        }

        if (transaction.getCuentaOrigenId() == null) {
            return Mono.error(new IllegalArgumentException("La cuenta origen es requerida."));
        }

        Long cuentaId = Long.parseLong(transaction.getCuentaOrigenId());

        // Comunicar con AccountMs para actualizar el saldo de manera reactiva
        return accountService.depositar(cuentaId, transaction.getMonto())
                .flatMap(bankAccount -> {
                    // Establecer la fecha de la transacción
                    transaction.setFecha(LocalDateTime.now());
                    return transactionRepository.save(transaction);
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al procesar el depósito: " + e.getMessage())));
    }

    public Mono<Transaction> registrarRetiro(Transaction transaction) {
        // Validar que el tipo sea RETIRO
        if (!"RETIRO".equalsIgnoreCase(transaction.getTipo())) {
            return Mono.error(new IllegalArgumentException("El tipo de transacción debe ser RETIRO."));
        }

        // Validar datos de entrada
        if (transaction.getMonto() == null || transaction.getMonto() <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a cero."));
        }

        if (transaction.getCuentaOrigenId() == null) {
            return Mono.error(new IllegalArgumentException("La cuenta origen es requerida."));
        }

        Long cuentaId = Long.parseLong(transaction.getCuentaOrigenId());

        // Verificar saldo suficiente
        return accountService.retirar(cuentaId, transaction.getMonto())
                .flatMap(bankAccount -> {
                    if (bankAccount.getSaldo() < transaction.getMonto()) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente para realizar el retiro."));
                    }
                    // Establecer la fecha de la transacción
                    transaction.setFecha(LocalDateTime.now());
                    return transactionRepository.save(transaction);
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al procesar el retiro: " + e.getMessage())));
    }

    public Mono<Transaction> registrarTransferencia(Transaction transaction) {
        // Validar que el tipo sea TRANSFERENCIA
        if (!"TRANSFERENCIA".equalsIgnoreCase(transaction.getTipo())) {
            return Mono.error(new IllegalArgumentException("El tipo de transacción debe ser TRANSFERENCIA."));
        }

        // Validar datos de entrada
        if (transaction.getMonto() == null || transaction.getMonto() <= 0) {
            return Mono.error(new IllegalArgumentException("El monto debe ser mayor a cero."));
        }

        if (transaction.getCuentaOrigenId() == null || transaction.getCuentaDestinoId() == null) {
            return Mono.error(new IllegalArgumentException("Las cuentas origen y destino son requeridas."));
        }

        Long cuentaOrigenId = Long.parseLong(transaction.getCuentaOrigenId());
        Long cuentaDestinoId = Long.parseLong(transaction.getCuentaDestinoId());

        // Verificar saldo suficiente antes de realizar la transferencia
        return accountService.retirar(cuentaOrigenId, transaction.getMonto())
                .flatMap(cuentaOrigen -> {
                    if (cuentaOrigen.getSaldo() < transaction.getMonto()) {
                        return Mono.error(new IllegalArgumentException("Saldo insuficiente para realizar la transferencia."));
                    }
                    // Realizar el depósito en la cuenta destino
                    return accountService.depositar(cuentaDestinoId, transaction.getMonto())
                            .flatMap(cuentaDestino -> {
                                // Establecer la fecha de la transacción
                                transaction.setFecha(LocalDateTime.now());
                                return transactionRepository.save(transaction);
                            });
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al procesar la transferencia: " + e.getMessage())));
    }

    public Flux<Transaction> obtenerHistorialPorCuenta(String cuentaOrigenId) {
        return transactionRepository.findByCuentaOrigenId(cuentaOrigenId);
    }

    public Flux<Transaction> obtenerTodasLasTransacciones() {
        return transactionRepository.findAll();
    }
}