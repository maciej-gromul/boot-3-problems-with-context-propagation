package com.example.boot3tracingtest.service;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface TextWrappingService {
    Mono<String> wrap(String string);
}
