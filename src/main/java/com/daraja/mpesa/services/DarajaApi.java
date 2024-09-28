package com.daraja.mpesa.services;
import com.daraja.mpesa.dtos.*;

public interface DarajaApi {
    AccessTokenResponse getAccessToken();

    RegisterURLResponse registerURL();
    SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest);
    B2CTransactionSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest) throws Exception;
}
