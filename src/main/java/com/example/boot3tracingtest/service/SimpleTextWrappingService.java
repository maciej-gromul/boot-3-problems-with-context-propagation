package com.example.boot3tracingtest.service;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class SimpleTextWrappingService implements TextWrappingService {

    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @Override
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
                .doOnNext(wrapped -> log.info("Inside layer wrapped: {}", wrapped))
                .handle((s, sink) -> {

                    log.info("Inside layer In the handle {}", tracer.currentSpan().context().spanId());
                    sink.next(s);
                });
    }
}
