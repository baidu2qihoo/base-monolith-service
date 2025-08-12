package com.hugh.base.service.registry.repo;

import com.hugh.base.service.registry.model.ServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRegistryRepository extends JpaRepository<ServiceInstance, String> {
}
