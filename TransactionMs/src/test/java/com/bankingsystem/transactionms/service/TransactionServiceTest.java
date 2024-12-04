package com.bankingsystem.transactionms.service;

import com.bankingsystem.transactionms.client.AccountService;
import com.bankingsystem.transactionms.exception.ResourceNotFoundException;
import com.bankingsystem.transactionms.model.Transaction;
import com.bankingsystem.transactionms.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarDeposito_Positive() {
        // Datos de prueba
        Transaction transaction = new Transaction();
        transaction.setTipo("DEPOSITO");
        transaction.setMonto(500.0);
        transaction.setCuentaOrigenId("1");
        transaction.setFecha(LocalDateTime.now());

        when(accountService.depositar(1L, 500.0)).thenReturn(Mono.just(new com.bankingsystem.transactionms.model.BankAccount()));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        // Llamada al método
        Transaction result = transactionService.registrarDeposito(transaction).block();

        // Verificaciones
        assertNotNull(result);
        assertEquals("DEPOSITO", result.getTipo());
        assertEquals(500.0, result.getMonto());
        verify(accountService, times(1)).depositar(1L, 500.0);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testRegistrarDeposito_Negative_InvalidMonto() {
        // Datos de prueba
        Transaction transaction = new Transaction();
        transaction.setTipo("DEPOSITO");
        transaction.setMonto(-500.0);
        transaction.setCuentaOrigenId("1");

        // Verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.registrarDeposito(transaction).block()
        );

        assertEquals("El monto debe ser mayor a cero.", exception.getMessage());
        verify(accountService, never()).depositar(anyLong(), anyDouble());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testRegistrarTransferencia_Positive() {
        // Datos de prueba
        Transaction transaction = new Transaction();
        transaction.setTipo("TRANSFERENCIA");
        transaction.setMonto(300.0);
        transaction.setCuentaOrigenId("1");
        transaction.setCuentaDestinoId("2");
        transaction.setFecha(LocalDateTime.now());

        when(accountService.retirar(1L, 300.0)).thenReturn(Mono.just(new com.bankingsystem.transactionms.model.BankAccount()));
        when(accountService.depositar(2L, 300.0)).thenReturn(Mono.just(new com.bankingsystem.transactionms.model.BankAccount()));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));

        // Llamada al método
        Transaction result = transactionService.registrarTransferencia(transaction).block();

        // Verificaciones
        assertNotNull(result);
        assertEquals("TRANSFERENCIA", result.getTipo());
        assertEquals(300.0, result.getMonto());
        verify(accountService, times(1)).retirar(1L, 300.0);
        verify(accountService, times(1)).depositar(2L, 300.0);
        verify(transactionRepository, times(1)).save(transaction);
    }
}