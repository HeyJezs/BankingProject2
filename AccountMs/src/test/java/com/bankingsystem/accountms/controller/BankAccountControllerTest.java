package com.bankingsystem.accountms.controller;

import com.bankingsystem.accountms.model.BankAccount;
import com.bankingsystem.accountms.model.TipoCuenta;
import com.bankingsystem.accountms.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BankAccountController bankAccountController;

    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();
    }

    @Test
    void testGetAccountsByClienteId_Positive() throws Exception {
        // Datos de prueba
        Long clienteId = 1L;
        BankAccount account1 = new BankAccount();
        account1.setId(1L);
        account1.setClienteId(clienteId);
        account1.setNumeroCuenta("12345");
        account1.setSaldo(1000.0);
        account1.setTipoCuenta(TipoCuenta.AHORROS);

        BankAccount account2 = new BankAccount();
        account2.setId(2L);
        account2.setClienteId(clienteId);
        account2.setNumeroCuenta("67890");
        account2.setSaldo(5000.0);
        account2.setTipoCuenta(TipoCuenta.CORRIENTE);

        when(bankAccountService.getAccountsByClienteId(clienteId)).thenReturn(List.of(account1, account2));

        // Simulación de la petición GET
        mockMvc.perform(get("/cuentas/cliente/{clienteId}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(bankAccountService, times(1)).getAccountsByClienteId(clienteId);
    }

    @Test
    void testGetAccountsByClienteId_Negative() throws Exception {
        // Datos de prueba
        Long clienteId = 99L;

        when(bankAccountService.getAccountsByClienteId(clienteId)).thenReturn(List.of());

        // Simulación de la petición GET
        mockMvc.perform(get("/cuentas/cliente/{clienteId}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(bankAccountService, times(1)).getAccountsByClienteId(clienteId);
    }

    @Test
    void testDepositar_Positive() throws Exception {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 500.0;

        BankAccount account = new BankAccount();
        account.setId(cuentaId);
        account.setSaldo(1500.0);

        when(bankAccountService.depositar(cuentaId, monto)).thenReturn(account);

        // Simulación de la petición PUT
        mockMvc.perform(put("/cuentas/{cuentaId}/depositar", cuentaId)
                        .param("monto", monto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1500.0));

        verify(bankAccountService, times(1)).depositar(cuentaId, monto);
    }

    @Test
    void testDepositar_Negative_InvalidMonto() throws Exception {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = -500.0;

        when(bankAccountService.depositar(cuentaId, monto))
                .thenThrow(new IllegalArgumentException("El monto debe ser mayor a 0."));

        // Simulación de la petición PUT
        mockMvc.perform(put("/cuentas/{cuentaId}/depositar", cuentaId)
                        .param("monto", monto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El monto debe ser mayor a 0."));

        verify(bankAccountService, times(1)).depositar(cuentaId, monto);
    }

    @Test
    void testRetirar_Positive() throws Exception {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 500.0;

        BankAccount account = new BankAccount();
        account.setId(cuentaId);
        account.setSaldo(500.0);

        when(bankAccountService.retirar(cuentaId, monto)).thenReturn(account);

        // Simulación de la petición PUT
        mockMvc.perform(put("/cuentas/{cuentaId}/retirar", cuentaId)
                        .param("monto", monto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(500.0));

        verify(bankAccountService, times(1)).retirar(cuentaId, monto);
    }

    @Test
    void testRetirar_Negative_SaldoInsuficiente() throws Exception {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 1500.0;

        when(bankAccountService.retirar(cuentaId, monto))
                .thenThrow(new IllegalArgumentException("Saldo insuficiente para realizar el retiro."));

        // Simulación de la petición PUT
        mockMvc.perform(put("/cuentas/{cuentaId}/retirar", cuentaId)
                        .param("monto", monto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente para realizar el retiro."));

        verify(bankAccountService, times(1)).retirar(cuentaId, monto);
    }
}