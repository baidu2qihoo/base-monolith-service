package com.hugh.base.service.registry.entity;

import com.hugh.base.service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;

@Entity
@Table(name = "service_registry")
public class ServiceRegistryEntity extends BaseEntity {

    @Column(nullable = false)
    private String env;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "instance_id", nullable = false)
    private String instanceId;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private Integer port;

    @Lob
    @Column(columnDefinition = "json")
    private String metadata;

    @Column(name = "status")
    private String status;

    @Column(name = "last_heartbeat")
    private java.time.Instant lastHeartbeat;

    public ServiceRegistryEntity() {}

    // getters and setters
    public String getEnv(){ return env; }
    public void setEnv(String e){ this.env = e; }
    public String getTenantId(){ return tenantId; }
    public void setTenantId(String t){ this.tenantId = t; }
    public String getServiceName(){ return serviceName; }
    public void setServiceName(String s){ this.serviceName = s; }
    public String getInstanceId(){ return instanceId; }
    public void setInstanceId(String i){ this.instanceId = i; }
    public String getHost(){ return host; }
    public void setHost(String h){ this.host = h; }
    public Integer getPort(){ return port; }
    public void setPort(Integer p){ this.port = p; }
    public String getMetadata(){ return metadata; }
    public void setMetadata(String m){ this.metadata = m; }
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status = s; }
    public java.time.Instant getLastHeartbeat(){ return lastHeartbeat; }
    public void setLastHeartbeat(java.time.Instant t){ this.lastHeartbeat = t; }
}
