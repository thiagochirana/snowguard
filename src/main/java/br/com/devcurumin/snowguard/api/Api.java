package br.com.devcurumin.snowguard.api;

import br.com.devcurumin.snowguard.api.handlers.ListAllUsersHandler;
import br.com.devcurumin.snowguard.api.handlers.CommandHandler;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Api {

    private final JavaPlugin plugin;
    private HttpServer server;

    public Api(JavaPlugin plugin){
        this.plugin = plugin;
        this.server = null;
    }

    public void startHttpServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);

            // All routes and his handlers
            server.createContext("/api/admin/command", new CommandHandler(plugin));
            server.createContext("/api/admin/list_all_users", new ListAllUsersHandler(plugin));

            server.setExecutor(null);
            server.start();
            plugin.getLogger().info("HTTP server started on port 8080");
        } catch (IOException e) {
            plugin.getLogger().severe("Unable to start a HTTP server: " + e.getMessage());
        }
    }
}