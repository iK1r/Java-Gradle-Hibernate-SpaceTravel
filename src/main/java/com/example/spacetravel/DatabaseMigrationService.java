package com.example.spacetravel;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.MigrateResult;

public final class DatabaseMigrationService {
    private DatabaseMigrationService() {
    }

    public static void main(String[] args) {
        DatabaseConfig.prepareLocalDirectory();
        migrate(DatabaseConfig.JDBC_URL);
    }

    public static int migrate(String jdbcUrl) {
        Flyway flyway = Flyway.configure()
                .dataSource(jdbcUrl, DatabaseConfig.USER, DatabaseConfig.PASSWORD)
                .locations("classpath:db/migration")
                .load();

        MigrateResult result = flyway.migrate();
        System.out.println("Flyway migrations executed now: " + result.migrationsExecuted);
        for (MigrationInfo migration : flyway.info().applied()) {
            System.out.println(migration.getScript() + " - " + migration.getState());
        }
        return result.migrationsExecuted;
    }
}
