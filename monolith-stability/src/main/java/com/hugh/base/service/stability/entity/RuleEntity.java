package com.hugh.base.service.stability.entity;

import com.hugh.base.service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "rule_store")
public class RuleEntity extends BaseEntity {

    @Column(nullable = false)
    private String env;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(name = "rule_type", nullable = false)
    private String ruleType; // FLOW, DEGRADE, ROUTE, AUTH

    @Column(name = "rule_key", nullable = false)
    private String ruleKey;

    @Lob
    @Column(name = "rule_json")
    private String ruleJson;

    @Column(name = "active")
    private Boolean active;

    public RuleEntity() {}

    // getters/setters
    public String getEnv(){ return env; }
    public void setEnv(String e){ this.env = e; }
    public String getTenantId(){ return tenantId; }
    public void setTenantId(String t){ this.tenantId = t; }
    public String getServiceName(){ return serviceName; }
    public void setServiceName(String s){ this.serviceName = s; }
    public String getAppName(){ return appName; }
    public void setAppName(String a){ this.appName = a; }
    public String getRuleType(){ return ruleType; }
    public void setRuleType(String r){ this.ruleType = r; }
    public String getRuleKey(){ return ruleKey; }
    public void setRuleKey(String k){ this.ruleKey = k; }
    public String getRuleJson(){ return ruleJson; }
    public void setRuleJson(String j){ this.ruleJson = j; }
    public Boolean getActive(){ return active; }
    public void setActive(Boolean a){ this.active = a; }
}
