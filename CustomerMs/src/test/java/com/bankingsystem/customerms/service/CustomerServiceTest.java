package com.bankingsystem.customerms.service;

import com.bankingsystem.customerms.client.AccountClient;
import com.bankingsystem.customerms.model.Customer;
import com.bankingsystem.customerms.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountClient accountClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer_Positive() {
        // Datos de prueba
        Customer customer = new Customer();
        customer.setNombre("Juan");
        customer.setApellido("Perez");
        customer.setDni("12345678");
        customer.setEmail("juan.perez@example.com");

        when(customerRepository.existsByDni(customer.getDni())).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);

        // Llamada al método
        Customer resultado = customerService.createCustomer(customer);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(customerRepository, times(1)).existsByDni(customer.getDni());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testCreateCustomer_Negative_DuplicateDNI() {
        // Datos de prueba
        Customer customer = new Customer();
        customer.setDni("12345678");

        when(customerRepository.existsByDni(customer.getDni())).thenReturn(true);

        // Verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.createCustomer(customer)
        );

        assertEquals("El DNI ya está registrado.", exception.getMessage());
        verify(customerRepository, times(1)).existsByDni(customer.getDni());
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testDeleteCustomer_Positive() {
        Long clienteId = 1L;

        when(accountClient.getAccountsByClienteId(clienteId)).thenReturn(List.of());

        // Llamada al método
        customerService.deleteCustomer(clienteId);

        // Verificaciones
        verify(accountClient, times(1)).getAccountsByClienteId(clienteId);
        verify(customerRepository, times(1)).deleteById(clienteId);
    }

    @Test
    void testDeleteCustomer_Negative_ActiveAccounts() {
        Long clienteId = 1L;

        when(accountClient.getAccountsByClienteId(clienteId)).thenReturn(List.of(new Object()));

        // Verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.deleteCustomer(clienteId)
        );

        assertEquals("No se puede eliminar el cliente porque tiene cuentas activas.", exception.getMessage());
        verify(accountClient, times(1)).getAccountsByClienteId(clienteId);
        verify(customerRepository, never()).deleteById(clienteId);
    }
}