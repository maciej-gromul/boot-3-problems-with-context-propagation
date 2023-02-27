package com.example.boot3tracingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Boot3TracingTestApplication {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();

        SpringApplication.run(Boot3TracingTestApplication.class, args);
    }

}
