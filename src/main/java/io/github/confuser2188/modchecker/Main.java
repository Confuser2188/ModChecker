package io.github.confuser2188.modchecker;

import io.github.confuser2188.packetlistener.FieldName;
import io.github.confuser2188.packetlistener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Plugin plugin;
    private final int CONFIG_VERSION = 1;
    private PacketListener packetListener;

    public void onEnable() {
        plugin = this;

        // Warning
        if(!FieldName.getServerVersion().contains("v1_8"))
            Logger.getLogger("Minecraft").warning("[ModChecker] This plugin will only work on players that use 1.8-1.8.9");

        // Enable command
        Bukkit.getPluginCommand("modchecker").setExecutor(new CommandManager());

        // Config things
        saveDefaultConfig();
        this.checkConfig();

        // Register events
        packetListener = new PacketListener();
        Bukkit.getPluginManager().registerEvents(packetListener, this);
        Bukkit.getServer().getPluginManager().registerEvents(new CheckModule(), this);

        // Add players
        Bukkit.getOnlinePlayers().forEach(packetListener::addPlayer);
    }

    public void onDisable() {
        // Remove players
        Bukkit.getOnlinePlayers().forEach(packetListener::removePlayer);
    }

    private void checkConfig()
    {
        FileConfiguration config = this.getConfig();
        if(config.getString("config_version") == null || !config.getString("config_version").equalsIgnoreCase("" + CONFIG_VERSION))
        {
            File file = new File(getDataFolder(), "config.yml");
            if(file.exists())
            {
                File file2 = new File(getDataFolder(), "config_old.yml");
                if(file2.exists())
                    file2.delete();

                file.renameTo(new File(getDataFolder(), "config_old.yml"));
                saveDefaultConfig();
            }
        }
    }

    public static String applyColor(String str) {
        return str.replace("&", "§");
    }
}
