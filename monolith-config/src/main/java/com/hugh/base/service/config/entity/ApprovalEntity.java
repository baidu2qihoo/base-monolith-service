package com.hugh.base.service.config.entity;

import com.hugh.base.service.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "approval")
public class ApprovalEntity extends BaseEntity {

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(nullable = false)
    private String env;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "operator")
    private String operator;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    public ApprovalEntity() {}

    // getters and setters
    public String getEntityType(){ return entityType; }
    public void setEntityType(String e){ this.entityType = e; }
    public Long getEntityId(){ return entityId; }
    public void setEntityId(Long id){ this.entityId = id; }
    public String getEnv(){ return env; }
    public void setEnv(String env){ this.env = env; }
    public String getTenantId(){ return tenantId; }
    public void setTenantId(String t){ this.tenantId = t; }
    public String getOperator(){ return operator; }
    public void setOperator(String o){ this.operator = o; }
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status = s; }
    public String getComments(){ return comments; }
    public void setComments(String c){ this.comments = c; }
}
