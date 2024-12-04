package com.bankingsystem.transactionms.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions") // Define la colección en MongoDB
public class Transaction {

    @Id // Define el identificador único
    private String id;
    private String tipo; // DEPOSITO, RETIRO, TRANSFERENCIA
    private Double monto;
    private LocalDateTime fecha;
    private String cuentaOrigenId;
    private String cuentaDestinoId; // Solo para transferencias
}