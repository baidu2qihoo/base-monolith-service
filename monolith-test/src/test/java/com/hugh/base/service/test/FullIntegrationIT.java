package com.hugh.base.service.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.BindMode;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Full integration smoke test using Testcontainers (MySQL + Redis).
 */
public class FullIntegrationIT {

    static MySQLContainer<?> mysql;
    static GenericContainer<?> redis;

    @BeforeAll
    public static void setup() {
        mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                .withDatabaseName("appdb")
                .withUsername("appuser")
                .withPassword("change-me");
        mysql.start();
        redis = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
                .withExposedPorts(6379);
        redis.start();
        System.out.println("MySQL URL: " + mysql.getJdbcUrl());
    }

    @AfterAll
    public static void teardown() {
        if (mysql != null) mysql.stop();
        if (redis != null) redis.stop();
    }

    @Test
    public void testMysqlConnection() throws Exception {
        try (Connection conn = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword())) {
            try (Statement s = conn.createStatement()) {
                try (ResultSet rs = s.executeQuery("SELECT 1")) {
                    assertTrue(rs.next());
                }
            }
        }
    }
}
