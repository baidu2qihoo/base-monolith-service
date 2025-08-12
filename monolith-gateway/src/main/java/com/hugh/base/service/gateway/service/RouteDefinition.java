package com.hugh.base.service.gateway.service;

public class RouteDefinition {
    private String id;
    private String pathPrefix; // e.g., /api/v1/xxx
    private String targetService; // service name in registry
    private String tenant; // optional
    private int weight = 100;
    // getters/setters
    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getPathPrefix(){ return pathPrefix; }
    public void setPathPrefix(String p){ this.pathPrefix = p; }
    public String getTargetService(){ return targetService; }
    public void setTargetService(String s){ this.targetService = s; }
    public String getTenant(){ return tenant; }
    public void setTenant(String t){ this.tenant = t; }
    public int getWeight(){ return weight; }
    public void setWeight(int w){ this.weight = w; }
}
