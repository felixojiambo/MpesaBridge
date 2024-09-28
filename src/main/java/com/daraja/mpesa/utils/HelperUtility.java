package com.daraja.mpesa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

@Slf4j
public class HelperUtility {

    /**
     * Convert a given string to a Base64 encoded string.
     *
     * @param value the string value to be encoded
     * @return Base64 encoded string
     */
    public static String toBase64String(String value) {
        byte[] data = value.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Convert a given object to its JSON representation.
     *
     * @param object the object to be converted to JSON
     * @return JSON string of the object or null if conversion fails
     */
    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            log.error("Error converting object to JSON -> {}", exception.getMessage());
            return null;
        }
    }

    /**
     * Encrypts the initiator password using the public key from the certificate
     * and returns the encrypted password in Base64 format.
     *
     * @param initiatorPassword the password to encrypt
     * @return the encrypted password as a Base64 string
     * @throws Exception if an encryption error occurs
     */
    public static String getSecurityCredentials(String initiatorPassword) throws Exception {
        try {
            // Convert initiator password to bytes
            byte[] input = initiatorPassword.getBytes(StandardCharsets.UTF_8);

            // Load the certificate from the classpath
            ClassPathResource resource = new ClassPathResource("SandboxCertificate.cer");
            InputStream inputStream = resource.getInputStream();

            // Load the public key from the certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(inputStream);
            PublicKey publicKey = certificate.getPublicKey();

            // Initialize the cipher for encryption using SunJCE (default provider)
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt the password
            byte[] cipherText = cipher.doFinal(input);

            // Encode the result in Base64
            return Base64.getEncoder().encodeToString(cipherText).trim();

        } catch (NoSuchAlgorithmException | CertificateException | InvalidKeyException | NoSuchPaddingException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error generating security credentials -> {}", e.getLocalizedMessage());
            throw e;
        } catch (IOException e) {
            log.error("IOException occurred -> {}", e.getMessage());
            throw e;
        }
    }
}
