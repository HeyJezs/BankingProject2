package com.bankingsystem.customerms.repository;

import com.bankingsystem.customerms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByDni(String dni);
}