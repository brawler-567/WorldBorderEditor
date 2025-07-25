package com.Z1dexRu.worldBorderEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class WorldBorderEditor extends JavaPlugin {

    private int dayBorderIncrease;
    private int nightBorderIncrease;
    private int dayStartHour;
    public long changeSpeed;
    private boolean isDayTime = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        Bukkit.broadcastMessage(ChatColor.GREEN + "[WorldBorderEditor] Плагин успешно запущен!");
        checkTimeAndUpdateWorldBorder();

        new BukkitRunnable() {
            @Override
            public void run() {
                checkTimeAndUpdateWorldBorder();
            }
        }.runTaskTimer(this,0, 20 * 60);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        config.addDefault("day-border-increase", 100000);
        config.addDefault("night-border-increase", 10000);
        config.addDefault("day-start-hour", 8);
        config.addDefault("change-speed-seconds", 6000000);

        config.options().copyDefaults(true);
        saveConfig();

        // Загружаем значения из конфига
        dayBorderIncrease = config.getInt("day-border-increase");
        nightBorderIncrease = config.getInt("night-border-increase");
        dayStartHour = config.getInt("day-start-hour");
        changeSpeed = config.getLong("change-speed-seconds");
    }

    private void checkTimeAndUpdateWorldBorder() {
        long currentTime = System.currentTimeMillis();
        long hours = (currentTime / (1000 * 60 * 60)) % 24;
        boolean nowIsDayTime = hours >= dayStartHour;
        if (nowIsDayTime != isDayTime) {
            isDayTime = nowIsDayTime;
            if (isDayTime) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder add " + dayBorderIncrease + " " + changeSpeed);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[WorldBorderEditor] изменение параметров барьера");
            }
            else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder add " + nightBorderIncrease + " " + changeSpeed);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[WorldBorderEditor] изменение параметров барьера");
            }
        }
    }
    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + "[WorldBorderEditor] Плагин отключен");
    }
}