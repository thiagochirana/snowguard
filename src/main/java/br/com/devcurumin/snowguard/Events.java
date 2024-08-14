package br.com.devcurumin.snowguard;

import br.com.devcurumin.snowguard.models.players.UserServer;
import br.com.devcurumin.snowguard.utils.Dates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Events implements Listener {

    private final JavaPlugin plugin;

    public Events(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String nick = event.getPlayer().getName();
        String now = Dates.nowStr();

        UserServer us = new UserServer(event.getPlayer().getUniqueId().toString(), plugin);
        if (us.existsSavedInDb()) {
            us.loadFromDb();
            us.setLastLoginDate(now);
            us.update();
        } else {
            us.setLastLoginDate(now);
            us.setNickname(nick);
            us.setFirstLoginDate(now);
            us.save();
        }
        plugin.getLogger().info("Player " + nick + " joined " + now);
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