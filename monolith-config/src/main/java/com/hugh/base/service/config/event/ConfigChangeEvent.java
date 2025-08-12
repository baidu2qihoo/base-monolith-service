package com.hugh.base.service.config.event;

import org.springframework.context.ApplicationEvent;

public class ConfigChangeEvent extends ApplicationEvent {
    private final String env;
    private final String tenant;
    private final String service;
    private final String app;
    private final Long version;
    private final String key;

    public ConfigChangeEvent(Object source, String env, String tenant, String service, String app, Long version, String key) {
        super(source);
        this.env = env; this.tenant = tenant; this.service = service; this.app = app; this.version = version; this.key = key;
    }

    public String getEnv(){ return env; }
    public String getTenant(){ return tenant; }
    public String getService(){ return service; }
    public String getApp(){ return app; }
    public Long getVersion(){ return version; }
    public String getKey(){ return key; }
}
