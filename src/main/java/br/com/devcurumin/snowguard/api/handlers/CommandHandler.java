package br.com.devcurumin.snowguard.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommandHandler extends MainHandler implements HttpHandler {
    public CommandHandler(JavaPlugin plugin){
        super(plugin);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        plugin.getLogger().info("Receive a request [" + exchange.getRequestMethod() + "] " + exchange.getRequestURI() + " >> " + exchange.getRequestBody());

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String command = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            plugin.getLogger().info("Trying to execute a command >> " + command);

            // Executar o comando na thread principal do Minecraft
            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean commandSuccess = false;
                    try {
                        commandSuccess = plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
                    } catch (Exception e) {
                        String r = "Unable to execute command: " + command + " -- > " + e.getMessage();
                        sendResponse(exchange, r, 418);
                        return;
                    }
                    plugin.getLogger().info("Command executed successfully (or not)...");

                    String response;
                    int statusCode;

                    if (commandSuccess) {
                        response = "Success to execute >> " + command;
                        statusCode = 200;
                    } else {
                        response = "Fail to execute command >> " + command;
                        statusCode = 418;
                    }

                    plugin.getLogger().info(response);
                    sendResponse(exchange, response, statusCode);
                }
            }.runTask(plugin);
        } else {
            String response = "Method not supported: " + exchange.getRequestMethod();
            sendResponse(exchange, response, 405);
        }
    }
}