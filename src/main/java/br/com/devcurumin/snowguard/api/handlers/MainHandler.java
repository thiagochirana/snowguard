package br.com.devcurumin.snowguard.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.OutputStream;

public class MainHandler {

    protected JavaPlugin plugin;

    public MainHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected void sendResponse(HttpExchange exchange, String response, int statusCode)  {
        try{
            exchange.sendResponseHeaders(statusCode, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            plugin.getLogger().info("Response: STATUS " + statusCode + " >> " + response);
        } catch (IOException e){
            plugin.getLogger().info("Unable to send a Response: STATUS " + statusCode + " | " + response + "\nReason: " + e.getMessage());
        }
    }
}