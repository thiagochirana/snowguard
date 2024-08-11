package br.com.devcurumin.snowguard.api.handlers;

public record PlayerJson(
        String uuid,
        String nickname,
        Boolean isAdmin,
        Boolean isBanned,
        double health,
        int level
) {
}