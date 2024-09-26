package com.daraja.mpesa.services;
import com.daraja.mpesa.dtos.AccessTokenResponse;
import com.daraja.mpesa.dtos.RegisterURLResponse;

public interface DarajaApi {
    AccessTokenResponse getAccessToken();

    RegisterURLResponse registerURL();
}
