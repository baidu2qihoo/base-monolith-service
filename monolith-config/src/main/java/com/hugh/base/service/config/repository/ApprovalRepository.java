package com.hugh.base.service.config.repository;

import com.hugh.base.service.config.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {
}
