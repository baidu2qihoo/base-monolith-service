package com.hugh.base.service.core.impl;
import com.hugh.base.service.core.api.TenantResolver;
public class DefaultTenantResolver implements TenantResolver {
    @Override public String resolveTenantId() { return "default"; }
}
