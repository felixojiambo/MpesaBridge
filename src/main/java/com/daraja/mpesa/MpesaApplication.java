package com.daraja.mpesa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MpesaApplication {

	public static void main(String[] args) {

		SpringApplication.run(MpesaApplication.class, args);
		System.out.println("Safaricom Mpesa Daraja");
	}

}
