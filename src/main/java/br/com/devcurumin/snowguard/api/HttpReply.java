package br.com.devcurumin.snowguard.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HttpReply implements Runnable {
    private Socket s;
    private StringBuilder body;
    private JavaPlugin javaPlugin;

    public HttpReply(Socket s, StringBuilder body, JavaPlugin javaPlugin) {
        this.s = s;
        this.body = body;
        this.javaPlugin = javaPlugin;
    }

    public void run() {
        try {
            javaPlugin.getLogger().info("Realizing a response...");
            PrintStream ps = new PrintStream(s.getOutputStream());
            ps.println("HTTP/1.1 200 OK");
            ps.println("Server: Minecraft Plugin called Snowguard");
            ps.println("Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT");
            ps.println("Content-Length: " + body.length());
            ps.println("Content-Type: text/html");
            ps.println("Connection: Closed");
            ps.println();
            ps.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}