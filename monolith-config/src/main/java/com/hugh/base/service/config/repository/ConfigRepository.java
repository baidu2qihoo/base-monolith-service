package com.hugh.base.service.config.repository;

import com.hugh.base.service.config.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {
    Optional<ConfigEntity> findByEnvAndTenantIdAndServiceNameAndAppNameAndConfigKey(String env, String tenantId, String serviceName, String appName, String configKey);
}
