package com.hugh.base.service.registry.dto;

public class HeartbeatRequest {
    private String env;
    private String instanceId;

    public HeartbeatRequest() {}
    public String getEnv(){ return env; }
    public void setEnv(String e){ this.env = e; }
    public String getInstanceId(){ return instanceId; }
    public void setInstanceId(String i){ this.instanceId = i; }
}
