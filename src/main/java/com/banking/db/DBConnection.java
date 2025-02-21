package com.banking.db;

import org.apache.ibatis.jdbc.ScriptRunner;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import static org.h2.tools.Server.openBrowser;

public class DBConnection implements QueryConstant {

    private static final String DRIVER = "org.h2.Driver";
    private static final String H2_CONSOLE_URL = "http://localhost:9090";
    private static final String SCHEMA_FILE_PATH = "src/main/resources/schema.sql";
    private static boolean h2ServerStarted = false;

    // Constructor (Runs once at startup)
    public DBConnection() {
        startH2WebConsole();
        initializeDatabase();
    }

    // Start H2 Web Console if not already started
    private void startH2WebConsole() {
        if (!h2ServerStarted) {
            try {
                org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-ifNotExists").start();
                openBrowser(H2_CONSOLE_URL);
                h2ServerStarted = true;
            } catch (Exception e) {
                System.err.println("⚠️ H2 Web Console could not start. It might already be running.");
            }
        }
    }

    // Load database properties from application.properties
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find application.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }
        return props;
    }

    // Establish and return a new database connection
    public Connection connect() throws SQLException, ClassNotFoundException {
        Properties props = loadProperties();
        Class.forName(DRIVER);
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
        );
    }

    // Initialize database tables if they do not exist
    private void initializeDatabase() {
        try (Connection connection = connect()) {
            if (isTableExists(connection, "ACCOUNTS")) {
                return; // Exit if the table already exists
            }
            executeSchemaScript(connection);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error initializing database", e);
        }
    }

    // Check if a table exists in the database
    private boolean isTableExists(Connection connection, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tableName.toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Execute the schema SQL script
    private void executeSchemaScript(Connection connection) {
        try (Reader reader = new BufferedReader(new FileReader(SCHEMA_FILE_PATH))) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setLogWriter(null);
            scriptRunner.runScript(reader);
        } catch (IOException e) {
            throw new RuntimeException("Error executing schema script", e);
        }
    }
}