package com.example.boot3tracingtest.debug;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;

@Log4j2
public class CustomLoggingExporter implements SpanExporter {
    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        for (SpanData span : spans) {
            log.info(
                    "{}[{}] -> Trace: {}\tSpan: {}\t Parent:{}\t Attr: {}",
                    span.getName(),
                    span.getKind(),
                    span.getTraceId(),
                    span.getSpanId(),
                    span.getParentSpanId(),
                    span.getAttributes()
            );
        }
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }
}
