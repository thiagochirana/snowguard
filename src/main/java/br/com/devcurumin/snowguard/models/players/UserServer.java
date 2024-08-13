package br.com.devcurumin.snowguard.models.players;

import br.com.devcurumin.snowguard.utils.Dates;
import org.bukkit.entity.Player;

public class UserServer {

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
}