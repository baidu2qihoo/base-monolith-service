package com.hugh.base.service.registry;

import com.hugh.base.service.registry.entity.ServiceRegistryEntity;
import com.hugh.base.service.registry.service.RegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegistryServiceTest {

    @Autowired
    private RegistryService service;

    @Test
    public void testRegisterAndDiscover() {
        ServiceRegistryEntity e = new ServiceRegistryEntity();
        e.setEnv("dev");
        e.setTenantId("tenantA");
        e.setServiceName("svc");
        e.setInstanceId("i1");
        e.setHost("127.0.0.1");
        e.setPort(8080);
        service.register(e);

        List<ServiceRegistryEntity> list = service.discover("dev", "svc");
        assertNotNull(list);
        assertTrue(list.size() >= 1);
    }
}
