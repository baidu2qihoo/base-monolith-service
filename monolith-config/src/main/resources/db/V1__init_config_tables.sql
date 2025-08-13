CREATE TABLE config_item (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             tenant_id VARCHAR(64) NOT NULL,
                             env VARCHAR(32) NOT NULL,
                             key_name VARCHAR(128) NOT NULL,
                             value TEXT NOT NULL,
                             version INT NOT NULL DEFAULT 1,
                             status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
