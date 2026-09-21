package com.rishabh.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @GetMapping("/employee")
    public Mono<ResponseEntity<String>> employeeFallBack(){
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE).
                body("Employee service is currently unavailable. Please try again later."));
    }


    @GetMapping("/address")
    public Mono<ResponseEntity<String>> addressFallBack(){
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE).
                body("Address service is currently unavailable. Please try again later."));
    }


    @GetMapping("/authService")
    public Mono<ResponseEntity<String>> authFallBack(){
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE).
                body("Auth service is currently unavailable. Please try again later."));
    }
}
