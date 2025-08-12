package com.hugh.base.service.config.repository;

import com.hugh.base.service.config.entity.ConfigVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigVersionRepository extends JpaRepository<ConfigVersionEntity, Long> {
}
