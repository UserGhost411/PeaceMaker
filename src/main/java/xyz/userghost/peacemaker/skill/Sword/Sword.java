package xyz.userghost.peacemaker.skill.Sword;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import xyz.userghost.peacemaker.rpgitems.RPG_Weapon;
import xyz.userghost.peacemaker.utils.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Sword implements Listener {
    Plugin my;
    Util util;
    RPG_Weapon rpgWeapon;
    public Sword(Plugin x) {
        my = x;
        rpgWeapon = new RPG_Weapon(my);
        util = new Util(my);
        Bukkit.getServer().getPluginManager().registerEvents(this,my);
        util.consolelog("Sword Class Loaded");
    }
    @EventHandler
    private void PlayerInteractEvent(PlayerInteractEvent e){
        try {
            if(e.getAction()==Action.RIGHT_CLICK_AIR){
                if(e.getPlayer().getInventory().getItemInMainHand().equals(rpgWeapon.excalibur(1))){
                    if(e.getPlayer().getCooldown(e.getPlayer().getInventory().getItemInMainHand().getType())==0) {
                        for(Player x : Bukkit.getOnlinePlayers()){
                            for (double xi = 0; xi < 2.5; xi+= 0.5) {
                                x.spawnParticle(Particle.CLOUD,e.getPlayer().getLocation().add(0,xi,0),0);
                            }
                        }
                        e.getPlayer().setCooldown(e.getPlayer().getInventory().getItemInMainHand().getType(),20*7);
                        e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1));
                    }
                }
            }
        }catch (Exception err){ util.consolelog(err.getMessage()); }
    }
}
