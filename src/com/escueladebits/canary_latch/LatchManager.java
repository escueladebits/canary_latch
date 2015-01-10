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

/**
 * This class of 
 */
public class LatchManager implements PluginListener {

    private String secret;
    private String key;

    private Latch latch;

    /**
     * Constructor.
     *
     * @param secret     The secret
     * @param key        The key
     */
    public LatchManager(String secret, String key) {
        this.secret = secret;
        this.key = key;
        latch = new Latch(this.secret, this.key);
    }

    /**
     * @param player     A Minecraft account
     */
    public void pairPlayer(Player player) {
        // TODO
    }

    /**
     * @param player    A Minecraft account
     */
    public void unpairPlayer(Player player) {
        // TODO
    }

    /**
     * Retrieves player latch status from latch service.ç
     * 
     * @param player     A Minecraft player.
     */
    public void updateStatus(Player player) {
        /*
        if !player.isBanned():
            latchAccount = getLatchAccount(player)
            boolean status = latch.retrieveStatus(latchAccount)
            if !status:
                latchBan(player)
        */
    }

    /**
     *
     */
    public boolean isLatchOut(Player player) {
        return true;
    }

    /**
     *
     */
    public void latchBan(Player player) {
        /*
        Canary.kick(player)
        Canary.ban(player)
        */
    } 

    private String getLatchAccount(Player player) {
        // return CanaryDB.getField(player, 'latch_account')
    }

    private void setLatchAccount(Player player, String latchAccount) {
        // CanaryDb.setField(player, 'latch_account', latchAccount)
    }

    private void removeLatchAccount(Player player) {
        setLatchAccount(player, '');
    }
}
