//package com.pica.srsms.Controller;
//
//import io.micrometer.tracing.Span;
//import io.micrometer.tracing.Tracer;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class TraceTestController {
//
//    private final Tracer tracer;
//
//    public TraceTestController(Tracer tracer) {
//        this.tracer = tracer;
//    }
//
//    @GetMapping("/trace-test")
//    public String testTracing() {
//        Span span = tracer.nextSpan().name("test-span").start();
//        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
//            return "TraceId: " + span.context().traceId();
//        } finally {
//            span.end();
//        }
//    }
//}