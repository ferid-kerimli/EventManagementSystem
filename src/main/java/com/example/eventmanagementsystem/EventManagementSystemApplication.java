package com.example.eventmanagementsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;

@SpringBootApplication
@EnableAsync
public class EventManagementSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EventManagementSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            if (logsDir.mkdirs()) {
                System.out.println("Logs directory created.");
            } else {
                System.err.println("Failed to create logs directory.");
            }
        }
    }
}
