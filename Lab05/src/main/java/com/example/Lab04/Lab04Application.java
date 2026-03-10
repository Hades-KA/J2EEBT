package com.example.Lab04;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class Lab04Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab04Application.class, args);
    }

    @Bean
    CommandLineRunner testConnection(DataSource dataSource) {
        return args -> {
            try (Connection c = dataSource.getConnection()) {
                System.out.println("CONNECTED OK: " + c.getMetaData().getURL());
            }
        };
    }
}