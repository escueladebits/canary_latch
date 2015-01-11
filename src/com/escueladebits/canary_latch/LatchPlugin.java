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

import net.canarymod.plugin.Plugin;

/**
 * This class provides an additional security access layer to a CanaryMod
 * server based in <a href="latch.elevenpaths.com">Latch</a>.
 * <p>
 * The class register listeners for reacting to user accesses or changes in the 
 * latch account. It also provides CLI commands to pair minecraft accounts with
 * its latch correspondant, to unpair it, and to update the status (banned or
 * not) of current users. 
 */
public class LatchPlugin extends Plugin {

    private LatchConfig config;
    private LatchManager latch;

    /**
     * Starts the plugin. Registers listeners for user connections and latch
     * updates, and register commands for pairing, unpairing and updating 
     * current users.
     *
     * @return	boolean
     */
    @Override
    public boolean enable() {
        config = new LatchConfig(this);
        String secretKey = config.getSecretKey();
        String applicationId = config.getApplicationId();

        latch = new LatchManager(secretKey, applicationId);

        // Register connection listener

        // Register pairing command

        // Register unpairing command

        // Register updating command

        latch.updateAll();
	return false;
    }

    /**
     * Stops the plugin.
     */
    @Override
    public void disable() {
    }
 }
