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
        if (latchAccount == "") {
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
        /*
        Canary.kick(player)
        Canary.ban(player)
        */
    } 

    private String getLatchAccount(Player player) {
        // return CanaryDB.getField(player, 'latch_account')
        return "";
    }

    private void setLatchAccount(Player player, String latchAccount) {
        // CanaryDB.setField(player, 'latch_account', latchAccount)
    }

    private String getLatchStatus(Player player) {
        // return CanaryDB.getField(player, 'latch_status')
        return "off";
    }

    private void setLatchStatus(Player player, String status) {
        // CanaryDB.setField(player, 'latch_status', status);
    }

    private void removeLatchAccount(Player player) {
        setLatchAccount(player, "");
    }
}
