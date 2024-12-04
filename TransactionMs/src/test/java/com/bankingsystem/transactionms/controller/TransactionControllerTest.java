package com.bankingsystem.transactionms.controller;

import com.bankingsystem.transactionms.model.Transaction;
import com.bankingsystem.transactionms.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testRegistrarDeposito_Positive() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTipo("DEPOSITO");
        transaction.setMonto(500.0);
        transaction.setCuentaOrigenId("1");
        transaction.setFecha(LocalDateTime.now());

        when(transactionService.registrarDeposito(any(Transaction.class))).thenReturn(Mono.just(transaction));

        mockMvc.perform(post("/transacciones/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"DEPOSITO\",\"monto\":500,\"cuentaOrigenId\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("DEPOSITO"))
                .andExpect(jsonPath("$.monto").value(500.0));

        verify(transactionService, times(1)).registrarDeposito(any(Transaction.class));
    }

    @Test
    void testRegistrarDeposito_Negative_InvalidData() throws Exception {
        mockMvc.perform(post("/transacciones/deposito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"DEPOSITO\",\"monto\":-500,\"cuentaOrigenId\":\"1\"}"))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).registrarDeposito(any(Transaction.class));
    }

    @Test
    void testRegistrarTransferencia_Positive() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTipo("TRANSFERENCIA");
        transaction.setMonto(300.0);
        transaction.setCuentaOrigenId("1");
        transaction.setCuentaDestinoId("2");
        transaction.setFecha(LocalDateTime.now());

        when(transactionService.registrarTransferencia(any(Transaction.class))).thenReturn(Mono.just(transaction));

        mockMvc.perform(post("/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"TRANSFERENCIA\",\"monto\":300,\"cuentaOrigenId\":\"1\",\"cuentaDestinoId\":\"2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$.monto").value(300.0));

        verify(transactionService, times(1)).registrarTransferencia(any(Transaction.class));
    }
}