package br.com.devcurumin.snowguard;

import br.com.devcurumin.snowguard.api.Api;
import br.com.devcurumin.snowguard.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Snowguard extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        this.getLogger().info("Plugin enabled!");

        // Caminho específico para o banco de dados
        // String dbPath = System.getProperty("user.home") + "/minecraft/database/snowguard.db";
        //databaseManager = new DatabaseManager(dbPath, this);
        // databaseManager.connect();

        //Starting API
        Api api = new Api(this);
        api.startHttpServer();

        getServer().getPluginManager().registerEvents(new Events(databaseManager, this), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled!");
        // databaseManager.close();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("hello")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("Hello "+player.getName()+"!");
            }
        }
        if (label.equalsIgnoreCase("boss")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage("your boss is coming now, press ALT TAB mothafuckar");
            }
        }
        return true;
    }


}