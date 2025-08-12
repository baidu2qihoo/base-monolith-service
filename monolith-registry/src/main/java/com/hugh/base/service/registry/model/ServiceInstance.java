package com.hugh.base.service.registry.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ServiceInstance {
    @Id
    private String instanceId;
    private String serviceName;
    private String host;
    private int port;
    private String status;
    private LocalDateTime lastHeartbeat;
}
