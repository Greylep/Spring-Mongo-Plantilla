package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamenApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamenApplication.class, args);
        // Mensaje opcional para saber que todo ha ido bien
        System.out.println("\n🚀 APLICACIÓN INICIADA CORRRECTAMENTE 🚀\n");
    }
}