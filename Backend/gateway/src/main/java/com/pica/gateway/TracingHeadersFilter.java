//package com.pica.gateway;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import java.util.Random;
//
//@Component
//public class TracingHeadersFilter implements GlobalFilter, Ordered {
//    private static final Logger log = LoggerFactory.getLogger(TracingHeadersFilter.class);
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // Create mutable copy of headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.putAll(exchange.getRequest().getHeaders());
//
//        // Add/override tracing headers
//        String traceId = headers.getFirst("X-B3-TraceId");
//        if (traceId == null) {
//            traceId = generateId();
//            headers.set("X-B3-TraceId", traceId);
//        }
//        headers.set("X-B3-SpanId", generateId());
//        headers.set("X-B3-Sampled", "1");
//
//        log.debug("Propagating tracing headers - TraceId: {}, Headers: {}", traceId, headers);
//
//        // Create new request with updated headers
//        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
//                .headers(h -> h.clear())
//                .headers(h -> h.putAll(headers))
//                .build();
//
//        return chain.filter(exchange.mutate().request(mutatedRequest).build());
//    }
//
//    private String generateId() {
//        return Long.toHexString(new Random().nextLong());
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.HIGHEST_PRECEDENCE + 100; // Run early
//    }
//}