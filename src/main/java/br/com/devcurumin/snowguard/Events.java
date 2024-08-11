package br.com.devcurumin.snowguard;

import br.com.devcurumin.snowguard.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class Events implements Listener {

    private final DatabaseManager databaseManager;
    private final JavaPlugin plugin;

    public Events(DatabaseManager databaseManager, JavaPlugin javaPlugin) {
        this.databaseManager = databaseManager;
        this.plugin = javaPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String user = event.getPlayer().getName();
//        UUID uuid = event.getPlayer().getUniqueId();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        plugin.getLogger().info("Player " + user + " joined " + now);
//        try (Connection conn = databaseManager.getConnection()) {
//            // Check if player exists
//            String selectSQL = "SELECT * FROM players WHERE uuid = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
//                pstmt.setString(1, uuid.toString());
//                ResultSet rs = pstmt.executeQuery();
//
//                if (rs.next()) {
//                    // Update existing player
//                    plugin.getLogger().info("Update last join of Player " + user );
//                    String updateSQL = "UPDATE players SET data_ultima_entrada = ? WHERE uuid = ?";
//                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)) {
//                        updatePstmt.setString(1, now);
//                        updatePstmt.setString(2, uuid.toString());
//                        updatePstmt.executeUpdate();
//                    }
//                } else {
//                    // Insert new player
//                    plugin.getLogger().info("Player " + user + " joined in the server for the first time!");
//                    String insertSQL = "INSERT INTO players" +
//                            "(user, uuid, data_primeira_entrada, data_ultima_entrada, bloqueado, habilitar_cmd)" +
//                            "VALUES (?, ?, ?, ?, ?, ?)";
//                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSQL)) {
//                        insertPstmt.setString(1, user);
//                        insertPstmt.setString(2, uuid.toString());
//                        insertPstmt.setString(3, now);
//                        insertPstmt.setString(4, now);
//                        insertPstmt.setBoolean(5, false);
//                        insertPstmt.setBoolean(6, true);
//                        insertPstmt.executeUpdate();
//                    }
//                }
//            } catch (SQLException e){
//                plugin.getLogger().warning("Unable to register new user => " + e.getMessage());
//            }
//        } catch (SQLException e) {
//            plugin.getLogger().warning("Unable to connect in database .db => " + e.getMessage());
//        } finally {
//            databaseManager.close();
//        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info("Player " + event.getPlayer().getName() + " goes out!");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        try{
            if (event.getEntity() instanceof Player){
                Player p = (Player) event.getEntity();
                if (p.isOp()){
                    event.setCancelled(true);
                    p.setHealth(p.getMaxHealth());
                }
            }
        } catch (Exception e){
            plugin.getLogger().severe(e.getMessage());
        }
    }

}