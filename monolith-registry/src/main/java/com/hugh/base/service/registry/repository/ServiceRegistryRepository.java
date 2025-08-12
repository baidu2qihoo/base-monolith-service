package com.hugh.base.service.registry.repository;

import com.hugh.base.service.registry.entity.ServiceRegistryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRegistryRepository extends JpaRepository<ServiceRegistryEntity, Long> {
    List<ServiceRegistryEntity> findByEnvAndServiceNameAndStatus(String env, String serviceName, String status);
    
    // 添加根据环境和实例ID查询的方法
    Optional<ServiceRegistryEntity> findByEnvAndInstanceId(String env, String instanceId);
}