package com.hugh.base.service.stability.repository;

import com.hugh.base.service.stability.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    List<RuleEntity> findByEnvAndTenantIdAndServiceNameAndActive(String env, String tenantId, String serviceName, Boolean active);
}
