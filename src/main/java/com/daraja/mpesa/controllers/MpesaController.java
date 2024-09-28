package com.daraja.mpesa.controllers;

import com.daraja.mpesa.dtos.*;
import com.daraja.mpesa.services.DarajaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mobile-money")
public class MpesaController {
    private final DarajaApi darajaApi;
    private final AcknowledgeResponse acknowledgeResponse;
    public MpesaController(DarajaApi darajaApi, AcknowledgeResponse acknowledgeResponse) {
        this.darajaApi = darajaApi;
        this.acknowledgeResponse = acknowledgeResponse;
    }

    @GetMapping(path = "/token", produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        return ResponseEntity.ok(darajaApi.getAccessToken());
    }

    @GetMapping(path = "/register-url", produces = "application/json")
    public ResponseEntity<RegisterURLResponse> registerURL() {
        return ResponseEntity.ok(darajaApi.registerURL());
    }

    @PostMapping(path = "/validation", produces = "application/json")
    public ResponseEntity<AcknowledgeResponse> mpesaValidation(@RequestBody MpesaValidationResponse mpesaValidationResponse) {
        return ResponseEntity.ok(acknowledgeResponse);
    }

    @PostMapping(path = "/simulate-c2b", produces = "application/json")
    public ResponseEntity<SimulateTransactionResponse> simulateB2CTransaction(@RequestBody SimulateTransactionRequest simulateTransactionRequest) {
        return ResponseEntity.ok(darajaApi.simulateC2BTransaction(simulateTransactionRequest));
    }
}

