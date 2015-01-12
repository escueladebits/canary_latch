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

/**
 * This class of 
 */
public class LatchManager {

    private String secretKey;
    private String applicationId;

    private Latch latch;

    /**
     * Constructor.
     *
     * @param secret     The secret
     * @param key        The key
     */
    public LatchManager(String applicationId, String secretKey) {
        this.secretKey = secretKey;
        this.applicationId = applicationId;
        latch = new Latch(this.applicationId, this.secretKey);
    }

    /**
     *
     * @param player     A Minecraft account
     * @param token      A latch token the user received in her phone
     */
    public void pairPlayer(Player player, String token) {
        LatchResponse response = latch.pair(token);
        if (response.getError() == null) {
            JsonObject data = response.getData();
            String latchAccount = data.get("accountId").getAsString();
            setLatchAccount(player, latchAccount);
        }
        else {
            // TODO: manage error
        }
    }

    /**
     *
     * @param player    A Minecraft account
     */
    public void unpairPlayer(Player player) {
        String latchAccount = getLatchAccount(player);
        latch.unpair(latchAccount);
    }

    /**
     * Retrieves player latch status from latch service.
     * 
     * @param player     A Minecraft player.
     */
    public void updateStatus(Player player) {
        String latchAccount = getLatchAccount(player);
        if (latchAccount != "") {
            LatchResponse response = latch.status(latchAccount);
            if (response.getError() == null) {
                JsonObject data = response.getData();
                JsonObject operations = data.getAsJsonObject("operations");
                JsonObject application = operations.getAsJsonObject(applicationId);
                String status = application.get("status").getAsString();
                setLatchStatus(player, status);
            }
            else {
                // TODO: manage Error
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

    /**
     *
     * @param player     A Minecraft player
     */
    public void latchBan(Player player) {
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
        }
        catch (DatabaseWriteException ex) {
            // TODO: Problems to solve
        }
    }

    private String getLatchAccount(Player player) {
        LatchDataAccess dataAccess = getLatchData(player);
        return dataAccess.latchAccount;
    }

    private void setLatchAccount(Player player, String latchAccount) {
        LatchDataAccess dataAccess = new LatchDataAccess();
        dataAccess.playerName = player.getName();
        dataAccess.latchAccount = latchAccount;
  
        updateLatchData(player, dataAccess);
    }

    private String getLatchStatus(Player player) {
        LatchDataAccess dataAccess = getLatchData(player);
        return dataAccess.latchStatus;
    }

    private void setLatchStatus(Player player, String status) {
        LatchDataAccess dataAccess = new LatchDataAccess();
        dataAccess.playerName = player.getName();
        dataAccess.latchStatus = status;

        updateLatchData(player, dataAccess);
    }

    private void removeLatchAccount(Player player) {
        setLatchAccount(player, "");
    }
}
