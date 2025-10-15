package com.app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DataSourceConfig {
    private static final HikariDataSource ds = init();

    private static HikariDataSource init() {
        HikariConfig cfg = new HikariConfig();

        // Read from environment variables
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null) {
            throw new IllegalStateException("DB_URL environment variable not set");
        }

        cfg.setJdbcUrl(url);
        cfg.setUsername(user);
        cfg.setPassword(pass);
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");

        cfg.setMaximumPoolSize(10);
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(10000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(1800000);
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(cfg);
    }

    public static DataSource get() {
        return ds;
    }
}