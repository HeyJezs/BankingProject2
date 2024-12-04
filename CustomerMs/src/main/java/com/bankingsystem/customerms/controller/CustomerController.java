package com.bankingsystem.customerms.controller;

import com.bankingsystem.customerms.model.Customer;
import com.bankingsystem.customerms.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Gestión de Clientes", description = "Operaciones relacionadas con la gestión de clientes")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un cliente con los datos proporcionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Obtener cliente por ID", description = "Obtiene los detalles de un cliente específico por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalles del cliente obtenidos exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public Customer getCustomerById(
            @Parameter(description = "ID del cliente a buscar") @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @Operation(summary = "Actualizar datos de un cliente", description = "Actualiza la información de un cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public Customer updateCustomer(
            @Parameter(description = "ID del cliente a actualizar") @PathVariable Long id,
            @RequestBody Customer customerDetails) {
        return customerService.updateCustomer(id, customerDetails);
    }

    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El cliente tiene cuentas activas y no puede ser eliminado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(
            @Parameter(description = "ID del cliente a eliminar") @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}