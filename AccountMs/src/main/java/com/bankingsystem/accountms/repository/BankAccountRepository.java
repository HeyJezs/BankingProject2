package com.bankingsystem.accountms.repository;

import com.bankingsystem.accountms.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByClienteId(Long clienteId);
}