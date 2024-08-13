package br.com.devcurumin.snowguard;

import br.com.devcurumin.snowguard.database.DatabaseManager;
import br.com.devcurumin.snowguard.database.repositories.UserServerRepository;
import br.com.devcurumin.snowguard.models.players.UserServer;
import br.com.devcurumin.snowguard.utils.Dates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Events implements Listener {

    private final UserServerRepository userServerRepository;
    private final JavaPlugin plugin;

    public Events(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        userServerRepository  = new UserServerRepository(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String nick = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();
        String now = Dates.nowStr();

        UserServer us = new UserServer();
        us.setNickname(nick);
        us.setPassword(null);
        us.setUuid(uuid);
        us.setLastLoginDate(now);

        UserServer u = userServerRepository.selectByUuid(uuid);
        if (u == null) {
            us.setFirstLoginDate(now);
            userServerRepository.insert(us);
        } else {
            userServerRepository.update(us);
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