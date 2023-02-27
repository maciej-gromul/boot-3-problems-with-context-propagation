package com.example.boot3tracingtest.controller;

import com.example.boot3tracingtest.service.TextWrappingService;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TheController {

    private final ObservationRegistry observationRegistry;

    private final TextWrappingService textWrappingService;

    @GetMapping("/trace")
    public Mono<String> trace() {
        return Mono.just(UUID.randomUUID().toString())
                .name("some-random-name")
                .tap(Micrometer.observation(observationRegistry))
                .doOnNext(uuid -> log.info("UUID: {}", uuid))
                .flatMap(textWrappingService::wrap)
                .doOnNext(wrapped -> log.info("Text: {}", wrapped));
    }
}
