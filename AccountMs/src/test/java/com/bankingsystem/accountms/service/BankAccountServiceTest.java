package com.bankingsystem.accountms.service;

import com.bankingsystem.accountms.exception.ResourceNotFoundException;
import com.bankingsystem.accountms.model.BankAccount;
import com.bankingsystem.accountms.model.TipoCuenta;
import com.bankingsystem.accountms.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountsByClienteId_Positive() {
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

        when(bankAccountRepository.findByClienteId(clienteId)).thenReturn(List.of(account1, account2));

        // Llamada al método
        List<BankAccount> cuentas = bankAccountService.getAccountsByClienteId(clienteId);

        // Verificaciones
        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
        verify(bankAccountRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    void testGetAccountsByClienteId_Negative() {
        // Datos de prueba
        Long clienteId = 99L;

        when(bankAccountRepository.findByClienteId(clienteId)).thenReturn(List.of());

        // Llamada al método
        List<BankAccount> cuentas = bankAccountService.getAccountsByClienteId(clienteId);

        // Verificaciones
        assertNotNull(cuentas);
        assertEquals(0, cuentas.size());
        verify(bankAccountRepository, times(1)).findByClienteId(clienteId);
    }

    @Test
    void testDepositar_Positive() {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 500.0;

        BankAccount account = new BankAccount();
        account.setId(cuentaId);
        account.setSaldo(1000.0);

        when(bankAccountRepository.findById(cuentaId)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(account);

        // Llamada al método
        BankAccount resultado = bankAccountService.depositar(cuentaId, monto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1500.0, resultado.getSaldo());
        verify(bankAccountRepository, times(1)).findById(cuentaId);
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void testDepositar_Negative_InvalidMonto() {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = -500.0;

        // Verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bankAccountService.depositar(cuentaId, monto)
        );

        assertEquals("El monto debe ser mayor a 0.", exception.getMessage());
        verify(bankAccountRepository, never()).findById(anyLong());
    }

    @Test
    void testDepositar_Negative_CuentaNoEncontrada() {
        // Datos de prueba
        Long cuentaId = 99L;
        Double monto = 500.0;

        when(bankAccountRepository.findById(cuentaId)).thenReturn(Optional.empty());

        // Verificar excepción
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bankAccountService.depositar(cuentaId, monto)
        );

        assertEquals("Cuenta bancaria no encontrada", exception.getMessage());
        verify(bankAccountRepository, times(1)).findById(cuentaId);
    }

    @Test
    void testRetirar_Positive() {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 500.0;

        BankAccount account = new BankAccount();
        account.setId(cuentaId);
        account.setSaldo(1000.0);
        account.setTipoCuenta(TipoCuenta.AHORROS);

        when(bankAccountRepository.findById(cuentaId)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(account);

        // Llamada al método
        BankAccount resultado = bankAccountService.retirar(cuentaId, monto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(500.0, resultado.getSaldo());
        verify(bankAccountRepository, times(1)).findById(cuentaId);
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void testRetirar_Negative_SaldoInsuficiente() {
        // Datos de prueba
        Long cuentaId = 1L;
        Double monto = 1500.0;

        BankAccount account = new BankAccount();
        account.setId(cuentaId);
        account.setSaldo(1000.0);
        account.setTipoCuenta(TipoCuenta.AHORROS);

        when(bankAccountRepository.findById(cuentaId)).thenReturn(Optional.of(account));

        // Verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bankAccountService.retirar(cuentaId, monto)
        );

        assertEquals("No se permite un saldo negativo para cuentas de ahorro.", exception.getMessage());
        verify(bankAccountRepository, times(1)).findById(cuentaId);
        verify(bankAccountRepository, never()).save(account);
    }
}