package com.hugh.base.service.config.entity;

import com.hugh.base.service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;

@Entity
@Table(name = "config_version")
public class ConfigVersionEntity extends BaseEntity {

    @Column(name = "config_id")
    private Long configId;

    @Column(nullable = false)
    private String env;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(name = "config_key", nullable = false)
    private String configKey;

    @Lob
    private String configValue;

    @Column(name = "version")
    private Long version;

    public ConfigVersionEntity() {}

    // getters and setters omitted for brevity
    public Long getConfigId(){ return configId; }
    public void setConfigId(Long id){ this.configId = id; }
    public String getEnv(){ return env; }
    public void setEnv(String env){ this.env = env; }
    public String getTenantId(){ return tenantId; }
    public void setTenantId(String t){ this.tenantId = t; }
    public String getServiceName(){ return serviceName; }
    public void setServiceName(String s){ this.serviceName = s; }
    public String getAppName(){ return appName; }
    public void setAppName(String a){ this.appName = a; }
    public String getConfigKey(){ return configKey; }
    public void setConfigKey(String k){ this.configKey = k; }
    public String getConfigValue(){ return configValue; }
    public void setConfigValue(String v){ this.configValue = v; }
    public Long getVersion(){ return version; }
    public void setVersion(Long v){ this.version = v; }
}
