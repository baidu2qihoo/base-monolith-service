package com.hugh.base.service.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    
    private final PresetConfigService presetConfigService;
    
    public DataSourceConfig(PresetConfigService presetConfigService) {
        this.presetConfigService = presetConfigService;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(presetConfigService.getProperty("spring.datasource.url", "jdbc:h2:mem:testdb"));
        cfg.setUsername(presetConfigService.getProperty("spring.datasource.username", "sa"));
        cfg.setPassword(presetConfigService.getProperty("spring.datasource.password", ""));
        cfg.setMaximumPoolSize(presetConfigService.getIntProperty("spring.datasource.maximum-pool-size", 10));
        cfg.setMinimumIdle(presetConfigService.getIntProperty("spring.datasource.minimum-idle", 5));
        cfg.setPoolName("HikariPool");
        return new HikariDataSource(cfg);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setPackagesToScan("com.hugh.base.service");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        emf.setJpaPropertyMap(props);
        return emf;
    }
}
