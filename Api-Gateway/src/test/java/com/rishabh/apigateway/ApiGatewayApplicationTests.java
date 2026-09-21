package com.rishabh.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApiGatewayApplicationTests {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
    }

    @Test
    void configuredRoutesAreLoaded() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();

        assertNotNull(routes);
        assertEquals(3, routes.size());
        assertTrue(routes.stream().anyMatch(route -> route.getId().equals("auth-service")
                && route.getUri().toString().equals("lb://auth-service")));
        assertTrue(routes.stream().anyMatch(route -> route.getId().equals("employee-service")
                && route.getUri().toString().equals("lb://employee-service")));
        assertTrue(routes.stream().anyMatch(route -> route.getId().equals("address-service")
                && route.getUri().toString().equals("lb://address-service")));
    }

}
