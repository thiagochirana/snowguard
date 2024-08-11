package br.com.devcurumin.snowguard.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private Connection connection;
    private final String dbPath;

    private final JavaPlugin plugin;

    public DatabaseManager(String dbPath, JavaPlugin plugin) {
        this.dbPath = dbPath;
        this.plugin = plugin;
    }

    public void connect() {
        try {
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();

            // Criar diretório se não existir
            if (!parentDir.exists()) {
                plugin.getLogger().info("[Snowguard] Creating database dirs...");
                parentDir.mkdirs();
            }

            // Criar arquivo do banco de dados se não existir
            if (!dbFile.exists()) {
                plugin.getLogger().info("[Snowguard] Creating database file...");
                dbFile.createNewFile();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTable();
        } catch (SQLException | IOException e) {
            plugin.getLogger().warning("[Snowguard] Unable to create a new database => "+ e.getMessage());
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                "user TEXT," +
                "uuid TEXT," +
                "data_primeira_entrada TEXT," +
                "data_ultima_entrada TEXT," +
                "bloqueado BOOLEAN," +
                "habilitar_cmd BOOLEAN" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            plugin.getLogger().info("[Snowguard] Creating Player table in database");
        } catch (SQLException e) {
            plugin.getLogger().warning("[Snowguard] Unable to create a new table in database.db => "+ e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}