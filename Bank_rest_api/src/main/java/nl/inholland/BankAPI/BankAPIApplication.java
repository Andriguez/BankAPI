package nl.inholland.BankAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BankAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankAPIApplication.class, args);
    }
}