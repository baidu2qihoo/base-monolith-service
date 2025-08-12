package com.hugh.base.service.config.entity;

import com.hugh.base.service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;

@Entity
@Table(name = "config")
public class ConfigEntity extends BaseEntity {

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
    @Column(name = "config_value")
    private String configValue;

    @Column(name = "status")
    private String status;

    public ConfigEntity() {}

    // getters and setters
    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
