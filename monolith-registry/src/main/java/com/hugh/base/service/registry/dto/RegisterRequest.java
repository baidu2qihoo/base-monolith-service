package com.hugh.base.service.registry.dto;

public class RegisterRequest {
    private String env;
    private String tenantId;
    private String serviceName;
    private String instanceId;
    private String host;
    private Integer port;
    private String metadata; // JSON string

    public RegisterRequest() {}
    // getters/setters
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
}
