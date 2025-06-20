package io.github.alprny.coldSnowballs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.Particle;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ColdSnowballs extends JavaPlugin implements Listener {

    private final Set<Snowball> snowballs = new HashSet<>();
    // Will be useful later
    private int freezetick;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Snowball sb : snowballs) {
                    Location loc = sb.getLocation();
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.SNOWFLAKE, loc, 1, 0.1, 0.1, 0.1, 0.02);
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onSnowballLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            snowballs.add(snowball);
        }
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            Location loc = snowball.getLocation();
            Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.ITEM_SNOWBALL, loc, 100, 0, 0, 0, 0.1);
            snowballs.remove(snowball);
        }
    }

    @EventHandler
    public void onSnowballHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball) {
            Entity entity = event.getEntity();
            freezetick = entity.getFreezeTicks();
            entity.setFreezeTicks(freezetick+5);
        }
    }
}