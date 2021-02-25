package xyz.userghost.peacemaker.module;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.userghost.peacemaker.utils.Region;
import xyz.userghost.peacemaker.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Limiter implements Listener {
    Plugin my;
    Util util;
    File configFile;
    FileConfiguration config;
    ArrayList<Region> regions = new ArrayList<Region>();
    public Limiter(Plugin x){
        my = x;
        util = new Util(x);
        Bukkit.getServer().getPluginManager().registerEvents(this,my);
        util.consolelog("Limiter Class Loaded");
        call_enabled();
    }
    @EventHandler
    public void spawnet (EntitySpawnEvent event) {
        if((event.getEntity() instanceof Monster || event.getEntity() instanceof Animals || event.getEntity() instanceof Bat) && !event.getEntity().hasMetadata("customnpc")) {
            for (Region re : regions) {
                if (re.isInRegion(event.getLocation())) {
                    
                    //util.consolelog(event.getEntity().getType().toString() + " is in spawn block: " + event.getLocation().getBlockX() + "/" + event.getLocation().getBlockY() + "/" + event.getLocation().getBlockZ());
                    event.setCancelled(true);
                }
            }
        }
    }

    public void call_enabled(){
        configFile = new File(my.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            util.copy(my.getResource("config.yml"), configFile,false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
            for(String le : config.getStringList("limiter.spawnlimit")){
                String dx[] = le.split(";");
                Location loc1 = new Location(Bukkit.getWorld(dx[0]),Double.valueOf(dx[1]),Double.valueOf(dx[2]),Double.valueOf(dx[3]));
                Location loc2 = new Location(Bukkit.getWorld(dx[4]),Double.valueOf(dx[5]),Double.valueOf(dx[6]),Double.valueOf(dx[7]));
                regions.add(new Region(loc1,loc2));
            }
        } catch (Exception e) {e.printStackTrace();}
    }

}