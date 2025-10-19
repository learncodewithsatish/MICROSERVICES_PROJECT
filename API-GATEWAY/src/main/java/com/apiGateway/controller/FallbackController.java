package com.apiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/employeeServiceFallback")
    public Mono<String> employeeFallbackMethod(){
        return Mono.just("Employee Service is down. Please try again later.");
    }

    @GetMapping("/addressServiceFallback")
    public Mono<String> addressFallbackMethod(){
        return Mono.just("Address Service is down. Please try again later.");
    }

}
