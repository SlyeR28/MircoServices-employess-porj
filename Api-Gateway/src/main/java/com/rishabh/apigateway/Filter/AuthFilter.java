package com.rishabh.apigateway.Filter;

import com.rishabh.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import java.nio.charset.StandardCharsets;


@Component
public class AuthFilter  extends  AbstractGatewayFilterFactory<AuthFilter.Config>{

    private final JwtUtil jwtUtil;

    public AuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");

            // Missing Header
            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                return unauthorized(exchange , "Missing or Invalid Authorization Header");
            }

            String token = authHeader.substring(7);


            try {
                // 2 Validate Signature + parse claims
                Claims claims = jwtUtil.parse(token);

                // 3 Reject expired token (JWT already throw  but explicit is clearer)
                if (jwtUtil.isExpired(claims)){
                    return unauthorized(exchange , "Token Expired");
                }

                // 4 propagate identity downStream
                 ServerHttpRequest mutated = request.mutate()
                         .header("X-User-Id" , claims.getSubject())
                         .header("X-User-Role" , String.valueOf(claims.get("role")))
                         .build();
                return chain.filter(exchange.mutate().request(mutated).build());
            } catch (JwtException | IllegalArgumentException e) {
                return unauthorized(exchange , "Invalid Token: " + e.getMessage());

            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange , String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = """
                {"status":401,"error":"Unauthorized","message":"%s"}
                """.formatted(message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));

    }

    public static class Config{

    }

}
