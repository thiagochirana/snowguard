package br.com.devcurumin.snowguard.database;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    protected Connection connection;
    protected final String dbPath;
    protected final JavaPlugin plugin;

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
                plugin.getLogger().info("Creating database dirs...");
                parentDir.mkdirs();
            }

            // Criar arquivo do banco de dados se não existir
            if (!dbFile.exists()) {
                plugin.getLogger().info("Creating database file...");
                dbFile.createNewFile();
            }

            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                plugin.getLogger().info("*** CONNECTED IN DATABASE "+dbPath+" ***");
            }
        } catch (SQLException | IOException e) {
            plugin.getLogger().warning("Unable to create a new database => "+ e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}