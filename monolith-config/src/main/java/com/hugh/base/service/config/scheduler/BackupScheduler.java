package com.hugh.base.service.config.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodic backup task for configuration snapshots.
 * In production this would export to S3 or remote storage.
 */
@Component
public class BackupScheduler {
    private static final Logger log = LoggerFactory.getLogger(BackupScheduler.class);

    @Scheduled(fixedDelayString = "${config.backup.interval.ms:3600000}") // default 1 hour
    public void backup() {
        log.info("Performing scheduled config backup...");
        // TODO: export config data from DB to backup storage (S3 / filesystem)
    }
}
