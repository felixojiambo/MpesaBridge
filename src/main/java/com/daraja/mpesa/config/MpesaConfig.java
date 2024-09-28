package com.daraja.mpesa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
public class MpesaConfig {

    private String consumerKey;
    private String consumerSecret;
    private String grantType;
    private String oauthEndpoint;
    private String registerUrlEndpoint;
    private String simulateTransactionEndpoint;
    private String shortCode;
    private String confirmationURL;
    private String validationURL;
    private String responseType;

}
