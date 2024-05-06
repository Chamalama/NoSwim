package com.cham.noswim;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public final class NoSwim extends JavaPlugin {

    public static NoSwim noSwim;

    @Override
    public void onEnable() {
        noSwim = this;
        getServer().getPluginManager().registerEvents(new SwimListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static NoSwim getNoSwim() {
        return noSwim;
    }
}
