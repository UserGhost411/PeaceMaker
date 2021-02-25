package xyz.userghost.peacemaker.skill.Magic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Summoner {
    Plugin my;
    //Util util;
    public Summoner(Plugin x) {
        my = x;
        //util = new Util(x);
    }
    public void zombiesummon(Player summoner, LivingEntity target,String level){
        int yy = summoner.getPlayer().getLocation().getBlockY();
        Location loc =new Location(summoner.getWorld(), summoner.getLocation().getBlockX()+3, yy, summoner.getLocation().getBlockZ()+3);
        if(summoner.getWorld().getBlockAt(loc).getType()!=Material.AIR){
            loc = null;
            loc = new Location(summoner.getWorld(), summoner.getLocation().getBlockX()+3, (yy+2), summoner.getLocation().getBlockZ()+3);
        }
        if(level.equals("low")) {
            Zombie z = (Zombie) summoner.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            z.setCustomName(summoner.getDisplayName() + " Guardian");
            z.setCustomNameVisible(true);
            z.setBaby(false);
            z.setMetadata("customnpc",new FixedMetadataValue(my, "infinityid"));
            z.setMetadata("cantburn", new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            z.setMetadata(summoner.getUniqueId().toString(), new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            z.setTarget(target);
            z.setRemoveWhenFarAway(true);
        }
        if(level.equals("med")) {
            Zombie z = (Zombie) summoner.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            z.setCustomName(summoner.getDisplayName() + " Guardian");
            z.setCustomNameVisible(true);
            z.setBaby(false);
            //z.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (6000), 255,false,false));
            z.setMetadata("customnpc",new FixedMetadataValue(my, "infinityid"));
            z.setMetadata("cantburn", new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            z.setMetadata(summoner.getUniqueId().toString(), new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            EntityEquipment ee = z.getEquipment();
            ee.setHelmet(new ItemStack(Material.LEATHER_HELMET, 1));
            ee.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
            ee.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS, 1));
            ee.setBoots(new ItemStack(Material.LEATHER_BOOTS, 1));
            ee.setItemInMainHand(new ItemStack(Material.STONE_SWORD, 1));
            ee.setBootsDropChance(0);
            ee.setChestplateDropChance(0);
            ee.setHelmetDropChance(0);
            ee.setLeggingsDropChance(0);
            ee.setItemInMainHandDropChance(0);
            z.setTarget(target);
            z.setRemoveWhenFarAway(true);
        }
        if(level.equals("high")) {
            Zombie z = (Zombie) summoner.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            z.setCustomName(summoner.getDisplayName() + " Guardian");
            z.setCustomNameVisible(true);
            z.setBaby(false);
            z.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, (6000), 4,false,false));
            z.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (6000), 3,false,false));
            z.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, (6000), 1,false,false));
            z.setMetadata("customnpc",new FixedMetadataValue(my, "infinityid"));
            z.setMetadata("cantburn", new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            z.setMetadata(summoner.getUniqueId().toString(), new FixedMetadataValue(my, summoner.getUniqueId().toString()));
            EntityEquipment ee = z.getEquipment();
            ee.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
            ee.setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
            ee.setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
            ee.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
            ee.setItemInMainHand(new ItemStack(Material.IRON_SWORD, 1));
            ee.setBootsDropChance(0);
            ee.setChestplateDropChance(0);
            ee.setHelmetDropChance(0);
            ee.setLeggingsDropChance(0);
            ee.setItemInMainHandDropChance(0);
            z.setTarget(target);
            z.setRemoveWhenFarAway(true);
        }
    }
    public void wallsummon(Location frontlocation,int to,Player p,Material mat){
        final World woldnya = p.getWorld();
        final ArrayList<Location> miaw = new ArrayList<Location>();
        if(to==1){
            miaw.add(frontlocation);
            miaw.add(frontlocation.add(0,1,0));
            miaw.add(frontlocation.add(0,-1,1));
            miaw.add(frontlocation.add(0,1,0));
            miaw.add(frontlocation.add(0,-1,-2));
            miaw.add(frontlocation.add(0,1,0));
        }else{
            miaw.add(frontlocation);
            miaw.add(frontlocation.add(0,1,0));
            miaw.add(frontlocation.add(1,-1,0));
            miaw.add(frontlocation.add(0,1,0));
            miaw.add(frontlocation.add(-2,-1,0));
            miaw.add(frontlocation.add(0,1,0));
        }
        for (Location nya : miaw){
            woldnya.getBlockAt(nya).setType(mat);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(my, new Runnable() {
            @Override
            public void run() {
                for (Location nya : miaw){
                    woldnya.getBlockAt(nya).setType(Material.AIR);
                }
            }
        },20*60);
    }
}
