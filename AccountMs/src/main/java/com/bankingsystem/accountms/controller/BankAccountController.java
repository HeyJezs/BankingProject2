package com.bankingsystem.accountms.controller;

import com.bankingsystem.accountms.model.BankAccount;
import com.bankingsystem.accountms.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<BankAccount>> getAccountsByClienteId(@PathVariable Long clienteId) {
        List<BankAccount> cuentas = bankAccountService.getAccountsByClienteId(clienteId);
        return ResponseEntity.ok(cuentas);
    }

    @PutMapping("/{cuentaId}/retirar")
    public ResponseEntity<BankAccount> retirar(@PathVariable Long cuentaId, @RequestParam Double monto) {
        BankAccount cuenta = bankAccountService.retirar(cuentaId, monto);
        return ResponseEntity.ok(cuenta);
    }

    @PutMapping("/{cuentaId}/depositar")
    public ResponseEntity<BankAccount> depositar(@PathVariable Long cuentaId, @RequestParam Double monto) {
        BankAccount cuenta = bankAccountService.depositar(cuentaId, monto);
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    public BankAccount createBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountService.createBankAccount(bankAccount);
    }

    @GetMapping
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccountById(@PathVariable Long id) {
        return bankAccountService.getBankAccountById(id);
    }

    @PutMapping("/{id}")
    public BankAccount updateBankAccount(@PathVariable Long id, @RequestBody BankAccount bankAccountDetails) {
        return bankAccountService.updateBankAccount(id, bankAccountDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.ok().build();
    }
}