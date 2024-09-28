package com.daraja.mpesa.config;
import com.daraja.mpesa.dtos.AcknowledgeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
    @Bean
    public AcknowledgeResponse acknowledgeResponse() {
        return new AcknowledgeResponse();
    }


}
