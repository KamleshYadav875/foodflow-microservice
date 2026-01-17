package com.foodflow.api_gateway.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodflow.api_gateway.dto.GatewayErrorResponse;
import com.foodflow.api_gateway.enums.UserRole;
import com.foodflow.api_gateway.exceptions.UnauthorizedException;
import com.foodflow.api_gateway.jwt.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public AuthenticationGatewayFilterFactory(
            JwtService jwtService,
            ObjectMapper objectMapper
    ) {
        super(Config.class);
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String header = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                return unauthorized(exchange, "Missing or invalid Authorization header");
            }

            String token = header.substring(7);

            try {
                Long userId = jwtService.getUserId(token);
                List<UserRole> roles = jwtService.getRoles(token);

                ServerHttpRequest mutatedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-USER-ID", String.valueOf(userId))
                        .header(
                                "X-ROLES",
                                roles.stream()
                                        .map(Enum::name)
                                        .collect(Collectors.joining(","))
                        )
                        .build();

                return chain.filter(
                        exchange.mutate().request(mutatedRequest).build()
                );

            } catch (UnauthorizedException ex) {
                log.warn("JWT validation failed: {}", ex.getMessage());
                return unauthorized(exchange, "Invalid or expired token");
            }
        };
    }

    private Mono<Void> unauthorized(
            org.springframework.web.server.ServerWebExchange exchange,
            String message
    ) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        GatewayErrorResponse error = GatewayErrorResponse.builder()
                .status(401)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        try {
            byte[] body = objectMapper.writeValueAsBytes(error);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(body);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

    @Data
    public static class Config {
        // future extension: requiredRoles, skipPaths, etc.
    }
}
