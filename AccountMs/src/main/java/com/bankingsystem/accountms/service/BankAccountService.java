package com.bankingsystem.accountms.service;

import com.bankingsystem.accountms.exception.ResourceNotFoundException;
import com.bankingsystem.accountms.model.BankAccount;
import com.bankingsystem.accountms.model.TipoCuenta;
import com.bankingsystem.accountms.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    // Metodo para obtener cuentas por clienteId
    public List<BankAccount> getAccountsByClienteId(Long clienteId) {
        return bankAccountRepository.findByClienteId(clienteId);
    }

    // Metodo para realizar retiros
    public BankAccount retirar(Long cuentaId, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        return bankAccountRepository.findById(cuentaId)
                .map(cuenta -> {
                    // Validaciones específicas por tipo de cuenta
                    if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS && cuenta.getSaldo() - monto < 0) {
                        throw new IllegalArgumentException("No se permite un saldo negativo para cuentas de ahorro.");
                    } else if (cuenta.getTipoCuenta() == TipoCuenta.CORRIENTE && cuenta.getSaldo() - monto < -500) {
                        throw new IllegalArgumentException("El sobregiro máximo para cuentas corrientes es de -500.");
                    }

                    // Realizar el retiro
                    cuenta.setSaldo(cuenta.getSaldo() - monto);
                    return bankAccountRepository.save(cuenta);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta bancaria no encontrada"));
    }

    // Metodo para realizar depósitos
    public BankAccount depositar(Long cuentaId, Double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        return bankAccountRepository.findById(cuentaId)
                .map(cuenta -> {
                    cuenta.setSaldo(cuenta.getSaldo() + monto);
                    return bankAccountRepository.save(cuenta);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta bancaria no encontrada"));
    }

    // Crear una nueva cuenta bancaria
    public BankAccount createBankAccount(BankAccount bankAccount) {
        if (bankAccount.getSaldo() <= 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor a 0.");
        }
        return bankAccountRepository.save(bankAccount);
    }

    // Obtener todas las cuentas bancarias
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    // Obtener una cuenta bancaria por ID
    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta bancaria no encontrada"));
    }

    // Actualizar una cuenta bancaria existente
    public BankAccount updateBankAccount(Long id, BankAccount bankAccountDetails) {
        return bankAccountRepository.findById(id)
                .map(bankAccount -> {
                    bankAccount.setNumeroCuenta(bankAccountDetails.getNumeroCuenta());
                    bankAccount.setTipoCuenta(bankAccountDetails.getTipoCuenta());
                    bankAccount.setSaldo(bankAccountDetails.getSaldo());
                    bankAccount.setClienteId(bankAccountDetails.getClienteId());
                    return bankAccountRepository.save(bankAccount);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta bancaria no encontrada"));
    }

    // Eliminar una cuenta bancaria
    public void deleteBankAccount(Long id) {
        Optional<BankAccount> account = bankAccountRepository.findById(id);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Cuenta bancaria no encontrada");
        }
        bankAccountRepository.deleteById(id);
    }
}