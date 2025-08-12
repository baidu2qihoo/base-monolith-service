package com.hugh.base.service.stability;

import com.hugh.base.service.stability.service.StabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StabilityServiceTest {

    @Autowired
    private StabilityService service;

    @Test
    public void testTryAcquireDefault() {
        // without rules defined, limiter allows (no limit)
        boolean ok = service.tryAcquire("nonexistent", 1);
        assertTrue(ok);
    }
}
