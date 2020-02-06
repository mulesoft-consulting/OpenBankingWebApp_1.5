package com.mulesoft.openbankingdemo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class OpenBankingSpringBootWebApplication {

	public static void main(String[] args) {
        SpringApplication.run(OpenBankingSpringBootWebApplication.class, args);
    }
}