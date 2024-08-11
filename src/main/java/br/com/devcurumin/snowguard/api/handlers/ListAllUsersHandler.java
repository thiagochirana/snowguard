package br.com.devcurumin.snowguard.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListAllUsersHandler extends MainHandler implements HttpHandler {

    public ListAllUsersHandler(JavaPlugin plugin){
        super(plugin);
    }

    @Override
    public void handle(HttpExchange exchange){
        plugin.getLogger().info("Receive a request [" + exchange.getRequestMethod() + "] " + exchange.getRequestURI() + " >> " + exchange.getRequestBody());
        if(!exchange.getRequestMethod().equalsIgnoreCase("GET")){
            String response = "Method not supported: " + exchange.getRequestMethod();
            sendResponse(exchange, response, 405);
            return;
        }

        Server server = plugin.getServer();
        Set<Player> players = new HashSet<>(server.getOnlinePlayers());
        for (OfflinePlayer p : server.getOfflinePlayers()){
            Player player = server.getPlayer(p.getUniqueId());
            players.add(player);
        }

        List<PlayerJson> playersJson = new ArrayList<>();
        for (Player p : players){
            playersJson.add(new PlayerJson(
                    p.getUniqueId().toString(),
                    p.getName(),
                    p.isOp(),
                    p.isBanned(),
                    p.getHealth(),
                    p.getLevel()
            ));
        }

        sendResponse(exchange, playersJson.toString() , 200);
    }
}