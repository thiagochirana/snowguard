package br.com.devcurumin.snowguard.models.players;

import br.com.devcurumin.snowguard.database.DatabaseRepository;
import br.com.devcurumin.snowguard.utils.Dates;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserServer extends DatabaseRepository {

    public Player player;
    public String nickname;
    public String password;
    public String uuid;
    public String firstLoginDate = Dates.nowStr();
    public String lastLoginDate;
    public Boolean enableLogin = false;
    public Boolean enableCommands = false;
    public Boolean hasInfinityLife = false;
    public Boolean isOp = false;

    public UserServer(String uuid, JavaPlugin plugin) {
        super(plugin);
        this.uuid = uuid;
        createTable();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstLoginDate() {
        return firstLoginDate;
    }

    public void setFirstLoginDate(String firstLoginDate) {
        this.firstLoginDate = firstLoginDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Boolean getEnableLogin() {
        return enableLogin;
    }

    public void setEnableLogin(Boolean enableLogin) {
        this.enableLogin = enableLogin;
    }

    public Boolean getEnableCommands() {
        return enableCommands;
    }

    public void setEnableCommands(Boolean enableCommands) {
        this.enableCommands = enableCommands;
    }

    public Boolean getHasInfinityLife() {
        return hasInfinityLife;
    }

    public void setHasInfinityLife(Boolean hasInfinityLife) {
        this.hasInfinityLife = hasInfinityLife;
    }

    public Boolean getIsOp() {
        return isOp;
    }

    public void setOp(Boolean op) {
        isOp = op;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "UserServer{" +
                "player=" + player +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", uuid='" + uuid + '\'' +
                ", firstLoginDate='" + firstLoginDate + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", enableLogin=" + enableLogin +
                ", enableCommands=" + enableCommands +
                ", hasInfinityLife=" + hasInfinityLife +
                ", isOp=" + isOp +
                '}';
    }

    // **************************************
    //          API PERSISTENCE
    // **************************************

    public void createTable() {
        connect();
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
                plugin.getLogger().info("CREATE_TABLE | table USER_SERVER created in database");
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("CREATE_TABLE FAILED | Unable to CREATE a new table in database.db => "+ e.getMessage());
        }
    }

    public boolean loadFromDb() {
        try {
            String selectSQL = "SELECT * FROM user_server WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(selectSQL);
            ps.setString(1, this.uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.setNickname(rs.getString("nickname"));
                this.setPassword(rs.getString("password"));
                this.setUuid(rs.getString("uuid"));
                this.setFirstLoginDate(rs.getString("first_login_date"));
                this.setLastLoginDate(rs.getString("last_login_date"));
                this.setEnableLogin(rs.getBoolean("enable_login"));
                this.setEnableCommands(rs.getBoolean("enable_commands"));
                this.setHasInfinityLife(rs.getBoolean("has_infinity_life"));
                this.setOp(rs.getBoolean("is_op"));
                plugin.getLogger().info("Selected user : " + this.toString());
                return true;
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("LOAD_FROM_DB FAILED | Unable to user a SELECT in user_server table: " + e.getMessage());
        }
        return false;
    }

    public boolean existsSavedInDb() {
        try {
            String selectSQL = "SELECT * FROM user_server WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(selectSQL);
            ps.setString(1, this.uuid);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            plugin.getLogger().warning("CHECK_IF_EXISTS FAILED | Unable to search in database: " + e.getMessage());
        }
        return false;
    }

    public boolean update() {
        StringBuilder updateSQL = new StringBuilder("UPDATE user_server SET ");
        List<Object> parameters = new ArrayList<>();

        if (this.getLastLoginDate() != null) {
            updateSQL.append("last_login_date = ?, ");
            parameters.add(this.getLastLoginDate());
        }

        if (this.getEnableLogin() != null) {
            updateSQL.append("enable_login = ?, ");
            parameters.add(this.getEnableLogin());
        }

        if (this.getEnableCommands() != null) {
            updateSQL.append("enable_commands = ?, ");
            parameters.add(this.getEnableCommands());
        }

        if (this.getHasInfinityLife() != null) {
            updateSQL.append("has_infinity_life = ?, ");
            parameters.add(this.getHasInfinityLife());
        }

        if (this.getIsOp() != null) {
            updateSQL.append("is_op = ?, ");
            parameters.add(this.getIsOp());
        }

        int lastCommaIndex = updateSQL.lastIndexOf(",");
        if (lastCommaIndex != -1) {
            updateSQL.deleteCharAt(lastCommaIndex);
        }

        updateSQL.append(" WHERE uuid = ?");
        parameters.add(this.getUuid());

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

    public boolean save() {
        String insertSQL = "INSERT INTO user_server " +
                "(nickname, password, uuid, first_login_date, last_login_date, enable_login, " +
                "enable_commands, has_infinity_life, is_op)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, this.getNickname());
            ps.setString(2, this.getPassword());
            ps.setString(3, this.getUuid());
            ps.setString(4, this.getFirstLoginDate());
            ps.setString(5, this.getLastLoginDate());
            ps.setBoolean(6, this.getEnableLogin());
            ps.setBoolean(7, this.getEnableCommands());
            ps.setBoolean(8, this.getHasInfinityLife());
            ps.setBoolean(9, this.getIsOp());
            int r = ps.executeUpdate();
            if (r > 0) {
                plugin.getLogger().info("SAVE | Inserted a new user server : " + this.toString());
                return true;
            } else {
                plugin.getLogger().info("SAVE FAILED | Failed to inserte a new user_server : " + this.toString());
            }
        } catch (SQLException e){
            plugin.getLogger().warning("SAVE FAILED | Unable to SAVE (INSERT) user_server table: " + e.getMessage());
        }
        return false;
    }
}