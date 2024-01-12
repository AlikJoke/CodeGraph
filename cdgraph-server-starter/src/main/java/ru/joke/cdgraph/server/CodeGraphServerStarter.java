package ru.joke.cdgraph.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeGraphServerStarter {

    public static void main(String[] args) {
        SpringApplication.run(CodeGraphServerStarter.class, args).registerShutdownHook();
    }
}
