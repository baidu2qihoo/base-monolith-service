package com.hugh.base.service.config.service;

import com.hugh.base.service.config.dto.PublishRequest;
import com.hugh.base.service.config.entity.ConfigEntity;
import com.hugh.base.service.config.entity.ConfigVersionEntity;
import com.hugh.base.service.config.event.ConfigChangeEvent;
import com.hugh.base.service.config.repository.ConfigRepository;
import com.hugh.base.service.config.repository.ConfigVersionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConfigService {

    private final ConfigRepository configRepository;
    private final ConfigVersionRepository versionRepository;
    private final ApplicationEventPublisher publisher;

    public ConfigService(ConfigRepository configRepository, ConfigVersionRepository versionRepository, ApplicationEventPublisher publisher) {
        this.configRepository = configRepository;
        this.versionRepository = versionRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Long publish(PublishRequest req) {
        Optional<ConfigEntity> opt = configRepository.findByEnvAndTenantIdAndServiceNameAndAppNameAndConfigKey(
                req.getEnv(), req.getTenantId(), req.getServiceName(), req.getAppName(), req.getConfigKey()
        );

        ConfigEntity cfg = opt.orElseGet(ConfigEntity::new);
        cfg.setEnv(req.getEnv());
        cfg.setTenantId(req.getTenantId());
        cfg.setServiceName(req.getServiceName());
        cfg.setAppName(req.getAppName());
        cfg.setConfigKey(req.getConfigKey());
        cfg.setConfigValue(req.getConfigValue());
        cfg.setStatus("ACTIVE");
        cfg = configRepository.save(cfg);

        // persist version
        ConfigVersionEntity ver = new ConfigVersionEntity();
        ver.setConfigId(cfg.getId());
        ver.setEnv(cfg.getEnv());
        ver.setTenantId(cfg.getTenantId());
        ver.setServiceName(cfg.getServiceName());
        ver.setAppName(cfg.getAppName());
        ver.setConfigKey(cfg.getConfigKey());
        ver.setConfigValue(cfg.getConfigValue());
        ver.setVersion(cfg.getVersion() == null ? 1L : cfg.getVersion() + 1);
        versionRepository.save(ver);

        // publish change event
        publisher.publishEvent(new ConfigChangeEvent(this, cfg.getEnv(), cfg.getTenantId(), cfg.getServiceName(), cfg.getAppName(), ver.getVersion(), cfg.getConfigKey()));
        return cfg.getId();
    }
}
