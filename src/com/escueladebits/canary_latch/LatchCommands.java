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

import net.canarymod.commandsys.CommandListener;
import net.canarymod.commandsys.Command;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.logger.Logman;

/**
 * This class of 
 */
public class LatchCommands implements CommandListener {
    
    private LatchManager latch;
    private LatchPlugin plugin;

    /**
     * Constructor.
     *
     * @param latch      A LatchManager, responsible to bind a Minecraft server
     *                   with the Latch service.
     */
    public LatchCommands (LatchPlugin plugin, LatchManager latch) {
        this.latch = latch;
        this.plugin = plugin;
    }

    /**
     *
     * @param caller     Who sent the message.
     * @param parameters Params
     */
    @Command(aliases = { "latch_pair" },
            description = "Pair a Minecraft player with a latch account.",
            permissions = { "" },
            toolTip = "/latch_pair <token>",
            min = 2)
    public void pairCommand(MessageReceiver caller, String[] parameters) {
        if (caller instanceof Player) {
            String token = parameters[1];
            latch.pairPlayer((Player)caller, token);
        }
        else {
            // TODO: Manage error
            plugin.getLogman().info("/latch_pair should be run by a player."); 
        }
    } 

    /**
     *
     * @param caller     Who sent the message
     * @param parameters Params
     */
    @Command(aliases = { "latch_unpair" },
            description = "Unpair a player from its latch account.",
            permissions = { "" },
            toolTip = "/latch_unpair",
            max = 1)
    public void unpairCommand(MessageReceiver caller, String[] parameters) {
        if (caller instanceof Player) {
            latch.unpairPlayer((Player)caller);
        }
    }

    /**
     *
     * @param caller     Who sent the message
     * @param parameters Params
     */
    @Command(aliases = { "latch_update" },
            description = "Update the Latch status of all current users.",
            permissions = { "" },
            toolTip = "/latch_update",
            max = 1)
    public void updateCommand(MessageReceiver caller, String[] parameters) {
        latch.updateAll();
    }
}
