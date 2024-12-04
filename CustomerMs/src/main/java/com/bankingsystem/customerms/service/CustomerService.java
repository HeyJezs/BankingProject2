package com.bankingsystem.customerms.service;

import com.bankingsystem.customerms.client.AccountClient;
import com.bankingsystem.customerms.model.Customer;
import com.bankingsystem.customerms.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountClient accountClient;

    // Constructor para inyectar dependencias
    public CustomerService(CustomerRepository customerRepository, AccountClient accountClient) {
        this.customerRepository = customerRepository;
        this.accountClient = accountClient;
    }

    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByDni(customer.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado.");
        }
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        customer.setNombre(customerDetails.getNombre());
        customer.setApellido(customerDetails.getApellido());
        customer.setDni(customerDetails.getDni());
        customer.setEmail(customerDetails.getEmail());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        // Llamar al cliente Feign para verificar si el cliente tiene cuentas activas
        List<Object> cuentas = accountClient.getAccountsByClienteId(id);
        if (!cuentas.isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar el cliente porque tiene cuentas activas.");
        }
        customerRepository.deleteById(id);
    }
}