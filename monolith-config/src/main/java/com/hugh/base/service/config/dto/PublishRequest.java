package com.hugh.base.service.config.dto;

public class PublishRequest {
    private String env;
    private String tenantId;
    private String serviceName;
    private String appName;
    private String configKey;
    private String configValue;
    private String operator;

    public PublishRequest() {}
    // getters/setters
    public String getEnv(){ return env; }
    public void setEnv(String e){ this.env = e; }
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
    public String getOperator(){ return operator; }
    public void setOperator(String o){ this.operator = o; }
}
