package com.hugh.base.service.gateway;

import com.hugh.base.service.gateway.service.RouteService;
import com.hugh.base.service.gateway.service.RouteDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RouteServiceTest {

    @Autowired
    private RouteService routeService;

    @Test
    public void testAddAndFind() {
        RouteDefinition d = new RouteDefinition();
        d.setId("r1");
        d.setPathPrefix("/proxy/test");
        d.setTargetService("svc");
        routeService.addOrUpdate(d);

        var opt = routeService.findByPath("/proxy/test/hello");
        assertTrue(opt.isPresent());
    }
}
