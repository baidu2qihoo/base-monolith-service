package com.hugh.base.service.core.api;
public interface IsolationContext {
    String getEnv();
    String getTenant();
    String getService();
    String getApp();
}
