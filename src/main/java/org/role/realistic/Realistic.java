package org.role.realistic;

import org.bukkit.plugin.java.JavaPlugin;

public final class Realistic extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Damage(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
