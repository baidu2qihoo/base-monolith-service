package com.hugh.base.service.config;

import com.hugh.base.service.config.PresetConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    private final PresetConfigService presetConfigService;

    public RedisConfig(PresetConfigService presetConfigService) {
        this.presetConfigService = presetConfigService;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        String redisHost = presetConfigService.getProperty("spring.redis.host", "localhost");
        int redisPort = presetConfigService.getIntProperty("spring.redis.port", 6379);

        RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(cfg);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory f) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(f);
        return template;
    }
}
