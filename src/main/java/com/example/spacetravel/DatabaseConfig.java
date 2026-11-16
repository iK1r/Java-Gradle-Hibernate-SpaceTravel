package com.example.spacetravel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DatabaseConfig {
    public static final String JDBC_URL = "jdbc:h2:./data/spacetravel";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    private DatabaseConfig() {
    }

    public static void prepareLocalDirectory() {
        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException e) {
            throw new DatabaseException("Cannot create local data directory", e);
        }
    }
}
