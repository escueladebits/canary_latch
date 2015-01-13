/*
Copyright © 2015 Antonio Jesús Sánchez Padial

This file is part of Canary Latch.

Canary Latch is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 2.1 of the License, or
(at your option) any later version.

Canary Latch is distributed in the hope that it will be useful
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with Canary Latch. If not see <http://www.gnu.org/licenses/>.
*/

package com.escueladebits.canary_latch;

import com.elevenpaths.latch.Latch;
import com.elevenpaths.latch.LatchResponse;
import com.elevenpaths.latch.Error;
import com.google.gson.JsonObject;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.PlayerManager;
import net.minecraft.server.MinecraftServer;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.database.exceptions.DatabaseReadException;
import java.util.HashMap;
import net.canarymod.logger.Logman;
import net.canarymod.Canary;

/**
 * This class of 
 */
public class LatchManager {

    private String secretKey;
    private String applicationId;

    private Latch latch;
    private LatchPlugin plugin;

    /**
     * Constructor.
     *
     * @param secret     The secret
     * @param key        The key
     */
    public LatchManager(LatchPlugin plugin, String appId, String secretKey) {
        this.secretKey = secretKey;
        this.applicationId = appId;
        latch = new Latch(this.applicationId, this.secretKey);
        this.plugin = plugin;
    }

    /**
     *
     * @param player     A Minecraft account
     * @param token      A latch token the user received in her phone
     */
    public void pairPlayer(Player player, String token) {
        String pName = player.getName();
        plugin.getLogman().info("Pairing " + pName + "using token " + token);
        LatchResponse response = latch.pair(token);
        if (response.getError() == null) {
            JsonObject data = response.getData();
            String latchAccount = data.get("accountId").getAsString();
            plugin.getLogman().info("Account " + latchAccount + " retrieved.");
            setLatchAccount(player, latchAccount);
        }
        else {
            // TODO: manage error
            plugin.getLogman().info("Fail pairing account.");
        }
    }

    /**
     *
     * @param player    A Minecraft account
     */
    public void unpairPlayer(Player player) {
        plugin.getLogman().info("Unpairing " + player.getName());
        String latchAccount = getLatchAccount(player);
        latch.unpair(latchAccount);
    }

    /**
     * Retrieves player latch status from latch service.
     * 
     * @param player     A Minecraft player.
     */
    public void updateStatus(Player player) {
        String name = player.getName();
        plugin.getLogman().info("Updating " + name + " latch status.");
        String latchAccount = getLatchAccount(player);
        if (latchAccount != "") {
            LatchResponse response = latch.status(latchAccount);
            if (response.getError() == null) {
                JsonObject data = response.getData();
                JsonObject operations = data.getAsJsonObject("operations");
                JsonObject app = operations.getAsJsonObject(applicationId);
                String status = app.get("status").getAsString();
                plugin.getLogman().info("latch(" + name + ") = " + status);
                setLatchStatus(player, status);
            }
            else {
                // TODO: manage Error
                plugin.getLogman().info("Status update failed.");
            }
        }
        else {
            // Clean status
            setLatchStatus(player, "");
        }
    }

    /**
     * Update Latch status for all current players.
     */
    public void updateAll() {
        // Tricky hack
        // https://github.com/CanaryModTeam/CanaryMod/blob/1.7.10-1.1.3/src/main/java/net/minecraft/server/MinecraftServer.java#L930
        //MinecraftServer minecraftServer = MinecraftServer.I();
        MinecraftServer minecraftServer = MinecraftServer.M();
        plugin.getLogman().info("Upadting all connected players!");

        for (World world: minecraftServer.worldManager.getAllWorlds()) {
            PlayerManager playerManager = world.getPlayerManager();
            for (Player player: playerManager.getManagedPlayers()) {
                updateStatus(player);
                if (isLatchOut(player)) {
                    latchBan(player);

                    // TODO: send a message to the banned player
                }
            }
        }
    }

    /**
     *
     * @param player     A Minecraft player
     */
    public boolean isLatchOut(Player player) {
        return getLatchStatus(player) == "off";
    }

    public void save(Player player, String account) {
        setLatchAccount(player, account);
    }

    /**
     *
     * @param player     A Minecraft player
     */
    public void latchBan(Player player) {
        plugin.getLogman().info("Latch banning " + player.getName());
        player.kick("Banned from Latch service.");
    } 

    private LatchDataAccess getLatchData(Player player) {
        LatchDataAccess dataAccess = new LatchDataAccess();
        try {
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("player_name", player.getName());
            Database.get().load(dataAccess, filter);
            
        }
        catch (DatabaseReadException ex) {
            // TODO: Problems to solve
        }
        return dataAccess;
    }

    private void updateLatchData(Player player, LatchDataAccess data) {
        HashMap<String, Object> filter = new HashMap<String, Object>();
        filter.put("player_name", player.getName());

        try {
            Database.get().update(data, filter);
            plugin.getLogman().info("Database write SUCCESS!");
        }
        catch (DatabaseWriteException ex) {
            // TODO: Problems to solve
            plugin.getLogman().info("FAIL updating.");
            plugin.getLogman().info(ex);
        }
    }

    private String getLatchAccount(Player player) {
        LatchDataAccess dataAccess = getLatchData(player);
        String name = player.getName(), account = dataAccess.latchAccount;
        plugin.getLogman().info("(" + name + ", " + account + ")");
        return dataAccess.latchAccount;
    }

    private void setLatchAccount(Player player, String account) {
        LatchDataAccess dataAccess = new LatchDataAccess();
        String name = player.getName();
        dataAccess.playerName = name;
        dataAccess.latchAccount = account;
        dataAccess.latchStatus = "on";
 
        plugin.getLogman().info("Setting (" + name + ", " + account + ")");
        plugin.getLogman().info("Default status: " + dataAccess.latchStatus);
        updateLatchData(player, dataAccess);
    }

    private String getLatchStatus(Player player) {
        LatchDataAccess dataAccess = getLatchData(player);
        String name = player.getName(), status = dataAccess.latchStatus;
        plugin.getLogman().info("(" + name + ", " + status + ")");
        return dataAccess.latchStatus;
    }

    private void setLatchStatus(Player player, String status) {
        LatchDataAccess dataAccess = getLatchData(player);
        dataAccess.latchStatus = status;

        plugin.getLogman().info("(" + player.getName() + ", " + status + ")");
        updateLatchData(player, dataAccess);
    }
}
