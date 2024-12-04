package com.bankingsystem.transactionms.controller;

import com.bankingsystem.transactionms.model.Transaction;
import com.bankingsystem.transactionms.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transacciones")
@Tag(name = "Gestión de Transacciones", description = "Operaciones relacionadas con transacciones bancarias")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Registrar un depósito",
            description = "Permite registrar un depósito en una cuenta bancaria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Depósito registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la transacción inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PostMapping("/deposito")
    public Mono<Transaction> registrarDeposito(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detalles del depósito a registrar",
                    required = true
            ) @RequestBody Transaction transaction) {
        return transactionService.registrarDeposito(transaction);
    }

    @Operation(
            summary = "Registrar un retiro",
            description = "Permite registrar un retiro de una cuenta bancaria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retiro registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la transacción inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PostMapping("/retiro")
    public Mono<Transaction> registrarRetiro(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detalles del retiro a registrar",
                    required = true
            ) @RequestBody Transaction transaction) {
        return transactionService.registrarRetiro(transaction);
    }

    @Operation(
            summary = "Registrar una transferencia",
            description = "Permite registrar una transferencia entre dos cuentas bancarias."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transferencia registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la transacción inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PostMapping("/transferencia")
    public Mono<Transaction> registrarTransferencia(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detalles de la transferencia a registrar",
                    required = true
            ) @RequestBody Transaction transaction) {
        return transactionService.registrarTransferencia(transaction);
    }

    @Operation(
            summary = "Obtener historial por cuenta",
            description = "Devuelve el historial de transacciones realizadas desde una cuenta específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de transacciones obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @GetMapping("/cuenta/{cuentaOrigenId}")
    public Flux<Transaction> obtenerHistorialPorCuenta(
            @Parameter(description = "ID de la cuenta origen", required = true)
            @PathVariable String cuentaOrigenId) {
        return transactionService.obtenerHistorialPorCuenta(cuentaOrigenId);
    }

    @Operation(
            summary = "Obtener historial completo de transacciones",
            description = "Devuelve el historial completo de todas las transacciones registradas en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Historial completo obtenido exitosamente")
    @GetMapping("/historial")
    public Flux<Transaction> obtenerHistorialCompleto() {
        return transactionService.obtenerTodasLasTransacciones();
    }
}