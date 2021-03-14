package com.github.lucavinci.bungeeban.player.models;

import lombok.Data;

import java.util.UUID;

/**
 * Represents a player that has joined the server at least once.
 * This keeps the most recent player name which is used to find the {@link UUID} of players.
 */
@Data
public class Player {

    /**
     * Unique player ID of the mojang account
     */
    private UUID uuid;

    /**
     * Latest player name associated with this player
     */
    private String name;

    /**
     * Timestamp of the last login of this player
     */
    private long lastLogin;

}
