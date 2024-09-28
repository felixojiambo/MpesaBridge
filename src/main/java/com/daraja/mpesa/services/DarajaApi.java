package com.daraja.mpesa.services;
import com.daraja.mpesa.dtos.AccessTokenResponse;
import com.daraja.mpesa.dtos.RegisterURLResponse;
import com.daraja.mpesa.dtos.SimulateTransactionRequest;
import com.daraja.mpesa.dtos.SimulateTransactionResponse;

public interface DarajaApi {
    AccessTokenResponse getAccessToken();

    RegisterURLResponse registerURL();
    SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest);
}
