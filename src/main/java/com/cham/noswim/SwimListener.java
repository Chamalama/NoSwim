package com.cham.noswim;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SwimListener implements Listener {

    public static List<UUID> playerSwimming = new ArrayList<>();

    private Map<UUID, BukkitTask> taskMap = new HashMap<>();

    public BukkitTask task;

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onTrySwim(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (p.isUnderWater() || p.getEyeLocation().subtract(0, 0.04, 0).getBlock().getType() == Material.WATER) {
            if (!playerSwimming.contains(p.getUniqueId())) {
                playerSwimming.add(p.getUniqueId());
            }
            checkSwim(p);
        } else {
            playerSwimming.remove(p.getUniqueId());
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onToggle(EntityToggleSwimEvent event) {
        if (event.getEntity() instanceof Player p) {
            p.setSprinting(false);
        }
    }

    public static void applySpeedToPlayer(Player player) {
        if (player.getEquipment().getBoots() != null) {
            ItemStack boots = player.getEquipment().getBoots();
            if (boots.containsEnchantment(Enchantment.DEPTH_STRIDER)) {
                int depthStriderLevel = boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, depthStriderLevel - 1, false, false));
            }
        }
    }

    private void checkSwim(Player p) {
        task = Bukkit.getScheduler().runTaskTimer(NoSwim.getNoSwim(), () -> {
            if (playerSwimming.contains(p.getUniqueId())) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2, -1, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2, -1, false, false));
                SwimListener.applySpeedToPlayer(p);
            } else {
                if (task != null) {
                    taskMap.get(p.getUniqueId()).cancel();
                }
            }
        }, 0L, 0L);
        taskMap.put(p.getUniqueId(), task);
    }
}
