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

/**
 * This class of 
 */
public class LatchCommands implements CommandListener {
    
    private LatchManager latch;

    /**
     * Constructor.
     *
     * @param latch      A LatchManager, responsible to bind a Minecraft server
     *                   with the Latch service.
     */
    public LatchCommands (LatchManager latch) {
        this.latch = latch;
    }

    /**
     *
     * @param caller     Who sent the message.
     * @param parameters Params
     */
    @Command(aliases = { "latch_pair" },
            description = { "Pair a Minecraft player with a latch account." },
            permissions = { "" },
            toolTip = "/latch_pair <token>",
            min = 2)
    public void pairCommand(MessageReceiver caller, String[] parameters) {
        Player player = caller.getPlayer();
        String token = parameters[1];
        latch.pairPlayer(player, token);
    } 

    /**
     *
     * @param caller     Who sent the message
     * @param parameters Params
     */
    @Command(aliases = { "latch_unpair" },
            description = { "Unpair a player from its latch account." },
            permissions = { "" },
            toolTip = "/latch_unpair",
            max = 1)
    public void unpairCommand(MessageReceiver caller, String[] parameters) {
        Player player = caller.getPlayer();
        latch.unpairPlayer(player);
    }

    /**
     *
     * @param caller     Who sent the message
     * @param parameters Params
     */
    @Command(aliases = { "latch_update" },
            description = { "Update the Latch status of all current users." },
            permissions = { "" },
            toolTip = "/latch_update",
            max = 1)
    public void updateCommand(MessageReceiver caller, String[] parameters) {
        latch.updateAll();
    }
}
