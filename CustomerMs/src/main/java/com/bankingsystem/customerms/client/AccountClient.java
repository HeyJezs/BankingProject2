package com.bankingsystem.customerms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "AccountMs", url = "http://localhost:8081/cuentas")
public interface AccountClient {
    @GetMapping("/cliente/{clienteId}")
    List<Object> getAccountsByClienteId(@PathVariable Long clienteId);
}