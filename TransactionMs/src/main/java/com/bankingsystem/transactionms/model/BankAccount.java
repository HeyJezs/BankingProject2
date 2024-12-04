package com.bankingsystem.transactionms.model;

import lombok.Data;

@Data
public class BankAccount {
    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private Double saldo;
    private Long clienteId;
}