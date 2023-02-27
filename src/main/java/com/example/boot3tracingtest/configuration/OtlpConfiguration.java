package com.example.boot3tracingtest.configuration;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationTextPublisher;
import io.micrometer.tracing.handler.TracingObservationHandler.TracingContext;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Log4j2
@Configuration
public class OtlpConfiguration {

    @Bean
    SpanExporter otelGrpcExporter() {
        return OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:4317")
                .build();
    }

    @Bean
    ObservationHandler<Observation.Context> observationTextPublisher() {
        return new ObservationTextPublisher(
                log::info,
                context -> true,
                context -> {
                    var tracingContext = context.<TracingContext>get(TracingContext.class);
                    var span = Optional.ofNullable(tracingContext).map(TracingContext::getSpan).orElse(null);

                    return context.getName() + " | " + context.getContextualName() + " -> " + (span != null ? span.context().traceId() + ":" + span.context().spanId() : String.valueOf(context));
                }
        );
    }
}
