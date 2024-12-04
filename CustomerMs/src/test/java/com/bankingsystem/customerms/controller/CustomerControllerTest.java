package com.bankingsystem.customerms.controller;

import com.bankingsystem.customerms.model.Customer;
import com.bankingsystem.customerms.service.CustomerService;
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

class CustomerControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testCreateCustomer_Positive() throws Exception {
        Customer customer = new Customer();
        customer.setNombre("Juan");
        customer.setApellido("Perez");
        customer.setDni("12345678");
        customer.setEmail("juan.perez@example.com");

        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"dni\":\"12345678\",\"email\":\"juan.perez@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void testCreateCustomer_Negative_InvalidData() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"apellido\":\"Perez\",\"dni\":\"\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(Customer.class));
    }

    @Test
    void testGetAllCustomers_Positive() throws Exception {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setNombre("Juan");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setNombre("Maria");

        when(customerService.getAllCustomers()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void testDeleteCustomer_Positive() throws Exception {
        Long clienteId = 1L;

        doNothing().when(customerService).deleteCustomer(clienteId);

        mockMvc.perform(delete("/clientes/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(clienteId);
    }
}