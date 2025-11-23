package com.groovin101.gow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Game of War web interface.
 * This runs alongside the existing command-line War.main() method.
 */
@SpringBootApplication
public class GameOfWarApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameOfWarApplication.class, args);
    }
}


