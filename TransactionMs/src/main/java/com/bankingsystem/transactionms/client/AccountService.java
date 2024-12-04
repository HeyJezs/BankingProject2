package com.bankingsystem.transactionms.client;

import com.bankingsystem.transactionms.model.BankAccount;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    private final WebClient webClient;

    public AccountService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/cuentas").build();
    }

    public Mono<BankAccount> depositar(Long cuentaId, Double monto) {
        return webClient.put()
                .uri("/{cuentaId}/depositar?monto={monto}", cuentaId, monto)
                .retrieve()
                .bodyToMono(BankAccount.class);
    }

    public Mono<BankAccount> retirar(Long cuentaId, Double monto) {
        return webClient.put()
                .uri("/{cuentaId}/retirar?monto={monto}", cuentaId, monto)
                .retrieve()
                .bodyToMono(BankAccount.class);
    }
}