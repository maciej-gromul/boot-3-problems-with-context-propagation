package com.example.boot3tracingtest.service;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SimpleTextWrappingService  {

    private final ObservationRegistry observationRegistry;

    public Mono<String> wrap(String string) {
        return Mono.just(string)
                .name("simple-text-wrapping-service")
                .tag("input", string)
                .tap(Micrometer.observation(observationRegistry))
                .map(str -> "Wrapped: " + str)
                .doOnNext(wrapped -> log.info("Inside wrapped: {}", wrapped))
                .flatMap(wrapped -> {
                    log.info("Wrapped flat map {}", wrapped);

                    return this.otherLayer(wrapped);
                })
                .handle((s, sink) -> {
                    log.info("In the handle");
                    sink.next(s);
                });
    }

    private Mono<String> otherLayer(String string) {
        return Mono.just(string)
                .name("other-layer")
                .tap(Micrometer.observation(observationRegistry))
                .map(str -> "Layer: " + str)
                .doOnNext(wrapped -> log.info("Inside layer wrapped: {}", wrapped));
    }
}
