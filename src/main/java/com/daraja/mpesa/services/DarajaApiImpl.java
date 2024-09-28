package com.daraja.mpesa.services;
import com.daraja.mpesa.config.MpesaConfig;
import com.daraja.mpesa.dtos.*;
import com.daraja.mpesa.utils.HelperUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.daraja.mpesa.utils.Constants.*;

@Slf4j
@Service
public class DarajaApiImpl implements DarajaApi {

    private final MpesaConfig mpesaConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public DarajaApiImpl(MpesaConfig mpesaConfig, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfig = mpesaConfig;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * @return Returns Daraja API Access Token Response
     */
    @Override
    public AccessTokenResponse getAccessToken() {
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s",
                mpesaConfig.getConsumerKey(), mpesaConfig.getConsumerSecret()));

        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfig.getOauthEndpoint(), mpesaConfig.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, AccessTokenResponse.class);
            } else {
                log.error("Response body is null");
                return null;
            }
        } catch (IOException e) {
            log.error("Could not get access token: {}", e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public RegisterURLResponse registerURL() {
        AccessTokenResponse accessTokenResponse = getAccessToken();

        RegisterURLRequest registerUrlRequest = new RegisterURLRequest();
        registerUrlRequest.setConfirmationURL(mpesaConfig.getConfirmationURL());
        registerUrlRequest.setResponseType(mpesaConfig.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfig.getShortCode());
        registerUrlRequest.setValidationURL(mpesaConfig.getValidationURL());


        RequestBody body = RequestBody.create(
                Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)).getBytes(StandardCharsets.UTF_8)
        );

        Request request = new Request.Builder()
                .url(mpesaConfig.getRegisterUrlEndpoint())
                .post(body)
                .addHeader("Authorization", String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), RegisterURLResponse.class);

        } catch (IOException e) {
            log.error("Could not register url -> {}", e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest) {
        AccessTokenResponse accessTokenResponse = getAccessToken();

        // Create request body using the updated method
        RequestBody body = RequestBody.create(
                Objects.requireNonNull(HelperUtility.toJson(simulateTransactionRequest)),
                JSON_MEDIA_TYPE
        );

        Request request = new Request.Builder()
                .url(mpesaConfig.getSimulateTransactionEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;

            // Use Jackson to decode the ResponseBody
            return objectMapper.readValue(response.body().string(), SimulateTransactionResponse.class);
        } catch (IOException e) {
            log.error("Could not simulate C2B transaction -> {}", e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public B2CTransactionSyncResponse performB2CTransaction(InternalB2CTransactionRequest internalB2CTransactionRequest) throws Exception {
        AccessTokenResponse accessTokenResponse = getAccessToken();
        log.info(String.format("Access Token: %s", accessTokenResponse.getAccessToken()));

        B2CTransactionRequest b2CTransactionRequest = new B2CTransactionRequest();

        b2CTransactionRequest.setCommandID(internalB2CTransactionRequest.getCommandID());
        b2CTransactionRequest.setAmount(internalB2CTransactionRequest.getAmount());
        b2CTransactionRequest.setPartyB(internalB2CTransactionRequest.getPartyB());
        b2CTransactionRequest.setRemarks(internalB2CTransactionRequest.getRemarks());
        b2CTransactionRequest.setOccassion(internalB2CTransactionRequest.getOccassion());

        // get the security credentials ...
        b2CTransactionRequest.setSecurityCredential(HelperUtility.getSecurityCredentials(mpesaConfig.getB2cInitiatorPassword()));

        // Instead of using String.format(), leverage slf4j's parameterized logging
        log.info("Security Creds: {}", b2CTransactionRequest.getSecurityCredential());

        // set the result url ...
        b2CTransactionRequest.setResultURL(mpesaConfig.getB2cResultUrl());
        b2CTransactionRequest.setQueueTimeOutURL(mpesaConfig.getB2cQueueTimeoutUrl());
        b2CTransactionRequest.setInitiatorName(mpesaConfig.getB2cInitiatorName());
        b2CTransactionRequest.setPartyA(mpesaConfig.getShortCode());

        RequestBody body = RequestBody.create(
                Objects.requireNonNull(HelperUtility.toJson(b2CTransactionRequest)),
                JSON_MEDIA_TYPE
        );
        Request request = new Request.Builder()
                .url(mpesaConfig.getB2cTransactionEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;

            return objectMapper.readValue(response.body().string(), B2CTransactionSyncResponse.class);
        } catch (IOException e) {
            log.error("Could not perform B2C transaction -> {}", e.getLocalizedMessage(), e);
            return null;
        }

    }
}