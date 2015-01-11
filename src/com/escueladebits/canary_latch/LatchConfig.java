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

import net.canarymod.config.Configuration;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.utils.PropertiesFile;

/**
 *
 */
class LatchConfig {
    private PropertiesFile config;
    private Plugin plugin;

    /**
     * Constructor.
     */
    public LatchConfig(Plugin plugin) {
        config = Configuration.getPluginConfig(plugin);
    }

    /**
     *
     */
    public String getSecretKey() {
        return config.getString("secret_key");
    }

    /**
     *
     */
    public String getApplicationId() {
        return config.getString("application_id");
    }
}
