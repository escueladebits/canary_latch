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

import net.canarymod.plugin.PluginListener;
import net.canarymod.hook.system.ServerTickHook;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.HookHandler;

/**
 * This class of 
 */
public class LatchListener implements PluginListener {

    private static THRESHOLD = 10000000;

    private long time0;
    private LatchManager latch;
    
    /**
     * Constructor.
     *
     * @param latch  A latch manager able to connect to the Latch server and
     *               retrieve information on users status, etc.
     */
    public LatchListener(LatchManager latch) {
        time0 = 0;
    }

    /**
     * Ban playing users whose latch status have changed since last.
     *
     * @param hook   A CanaryMod hook launched every time the game server clock
     *               ticks.
     */
    @HookHandler
    public void onTick(ServerTickHook hook) {
        time0 += hook.getDeltaTime();
        if (time0 >= THRESHOLD) {
            latch.updateAll();
            time0 = 0;
        }
    }

    /**
     * Checks connecting users againts their pair latch account and bans those
     * who are latched out.
     *
     * @param hook    A CanaryMod hook launched every time a player connects 
     *                to the server. It allows access to player's info.
     */
    @HookHandler
    public void onConnection(ConnectionHook hook) {
        Player player = hook.getPlayer();
        latch.updateStatus(player);
        if (latch.isLatchOut(player)) {
            latch.latchBan(player);
        }
        // set a message for ban user
    }
}
