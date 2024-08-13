package br.com.devcurumin.snowguard.database.repositories;

import br.com.devcurumin.snowguard.database.DatabaseRepository;
import br.com.devcurumin.snowguard.models.players.UserServer;
import br.com.devcurumin.snowguard.utils.Dates;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServerRepository extends DatabaseRepository {

    public UserServerRepository(JavaPlugin plugin) {
        super(plugin);
        createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user_server (" +
                "uuid TEXT PRIMARY KEY NOT NULL," +
                "nickname TEXT," +
                "password TEXT,"+
                "first_login_date TEXT," +
                "last_login_date TEXT," +
                "enable_login BOOLEAN," +
                "enable_commands BOOLEAN," +
                "has_infinity_life BOOLEAN," +
                "is_op BOOLEAN "+
                ");";

        try (Statement stmt = connection.createStatement()) {
            if (stmt.execute(sql)) {
                plugin.getLogger().info("table USER_SERVER created in database");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("CREATE_TABLE | Unable to CREATE a new table in database.db => "+ e.getMessage());
        }
    }

    public UserServer selectByUuid(String uuid) {
        try {
            String selectSQL = "SELECT * FROM user_server WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(selectSQL);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserServer us = new UserServer();
                us.setNickname(rs.getString("nickname"));
                us.setPassword(rs.getString("password"));
                us.setUuid(rs.getString("uuid"));
                us.setFirstLoginDate(rs.getString("first_login_date"));
                us.setLastLoginDate(rs.getString("last_login_date"));
                us.setEnableLogin(rs.getBoolean("enable_login"));
                us.setEnableCommands(rs.getBoolean("enable_commands"));
                us.setHasInfinityLife(rs.getBoolean("has_infinity_life"));
                us.setOp(rs.getBoolean("is_op"));
                plugin.getLogger().info("Selected user : " + us.toString());
                return us;
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("SELECT_BY_UUID | Unable to user a SELECT in user_server table: " + e.getMessage());
        }
        return null;
    }

    public boolean update(UserServer player) {
        StringBuilder updateSQL = new StringBuilder("UPDATE user_server SET ");
        List<Object> parameters = new ArrayList<>();

        if (player.getLastLoginDate() != null) {
            updateSQL.append("last_login_date = ?, ");
            parameters.add(player.getLastLoginDate());
        }

        if (player.getEnableLogin() != null) {
            updateSQL.append("enable_login = ?, ");
            parameters.add(player.getEnableLogin());
        }

        if (player.getEnableCommands() != null) {
            updateSQL.append("enable_commands = ?, ");
            parameters.add(player.getEnableCommands());
        }

        if (player.getHasInfinityLife() != null) {
            updateSQL.append("has_infinity_life = ?, ");
            parameters.add(player.getHasInfinityLife());
        }

        if (player.getIsOp() != null) {
            updateSQL.append("is_op = ?, ");
            parameters.add(player.getIsOp());
        }

        int lastCommaIndex = updateSQL.lastIndexOf(",");
        if (lastCommaIndex != -1) {
            updateSQL.deleteCharAt(lastCommaIndex);
        }

        updateSQL.append(" WHERE uuid = ?");
        parameters.add(player.getUuid());

        try (PreparedStatement up = connection.prepareStatement(updateSQL.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                up.setObject(i + 1, parameters.get(i));
            }
            return up.executeUpdate() > 0;
        } catch (SQLException e) {
            plugin.getLogger().warning("UPDATE FAIL | Unable to UPDATE user_server table: " + e.getMessage());
        }
        return false;
    }

    public boolean insert(UserServer p) {
        String insertSQL = "INSERT INTO user_server " +
                            "(nickname, password, uuid, first_login_date, last_login_date, enable_login, " +
                "enable_commands, has_infinity_life, is_op)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, p.getNickname());
            ps.setString(2, p.getPassword());
            ps.setString(3, p.getUuid());
            ps.setString(4, p.getFirstLoginDate());
            ps.setString(5, p.getLastLoginDate());
            ps.setBoolean(6, p.getEnableLogin());
            ps.setBoolean(7, p.getEnableCommands());
            ps.setBoolean(8, p.getHasInfinityLife());
            ps.setBoolean(9, p.getIsOp());
            int r = ps.executeUpdate();
            if (r > 0) {
                plugin.getLogger().info("INSERT | Inserted a new user server : " + p.toString());
                return true;
            } else {
                plugin.getLogger().info("INSERT FAIL | Inserted a new user server FAIL : " + p.toString());
            }
        } catch (SQLException e){
            plugin.getLogger().warning("INSERT | Unable to INSERT user_server table: " + e.getMessage());
        }
        return false;
    }

}