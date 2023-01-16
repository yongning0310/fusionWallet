package com.example.fusionwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@EnableConfigurationProperties
@Configuration
@SpringBootApplication
public class FusionWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(FusionWalletApplication.class, args);
    }

}
