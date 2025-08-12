package com.hugh.base.service.config;

import com.hugh.base.service.config.dto.PublishRequest;
import com.hugh.base.service.config.service.ConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConfigServiceTest {

    @Autowired
    private ConfigService service;

    @Test
    public void testPublish() {
        PublishRequest req = new PublishRequest();
        req.setEnv("dev");
        req.setTenantId("tenantA");
        req.setServiceName("svc");
        req.setAppName("app");
        req.setConfigKey("k");
        req.setConfigValue("v");
        Long id = service.publish(req);
        assertNotNull(id);
    }
}
