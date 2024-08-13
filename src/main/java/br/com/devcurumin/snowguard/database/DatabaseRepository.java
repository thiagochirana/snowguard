package br.com.devcurumin.snowguard.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class DatabaseRepository extends DatabaseManager {

    public DatabaseRepository(JavaPlugin plugin) {
        super("/home/curumin/minecraft/database/snowguard.db", plugin);
        connect();
    }
}