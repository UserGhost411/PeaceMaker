package xyz.userghost.peacemaker.skill.Magic;

import xyz.userghost.peacemaker.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BossBarData {
    BossBar bb1,bb2,bb3,bb4,bb5;
    Player p;
    Util util;
    public BossBarData(Player py,Util u){
        this.p = py;
        this.util = u;
        bb1 = Bukkit.createBossBar("...", BarColor.RED, BarStyle.SOLID);
        bb2 = Bukkit.createBossBar("...", BarColor.RED, BarStyle.SOLID);
        bb3 = Bukkit.createBossBar("...", BarColor.RED, BarStyle.SOLID);
        bb4 = Bukkit.createBossBar("...", BarColor.RED, BarStyle.SOLID);
        bb5 = Bukkit.createBossBar("...", BarColor.RED, BarStyle.SOLID);
        bb1.addPlayer(p);
        bb2.addPlayer(p);
        bb3.addPlayer(p);
        bb4.addPlayer(p);
        bb5.addPlayer(p);
    }
    public void closebb(){
        bb1.removeAll();
        bb2.removeAll();
        bb3.removeAll();
        bb4.removeAll();
        bb5.removeAll();
    }
    public void updatebb(){
        int i = 0;
        for (Entity et : util.getEntityInSight(p,15)) {
            if (et instanceof LivingEntity) {
                if (et == p) continue;
                LivingEntity let = (LivingEntity)et;
                i++;
                if (i == 1) {
                    double hp = let.getHealth();
                    double maxhp = let.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if(let.hasMetadata("fakehp")){
                        hp =  Math.round(((hp/2)-0.1)* 100.0) / 100.0;
                        maxhp =  Math.round((maxhp/2) * 100.0) / 100.0; }
                    bb1.setTitle(let.getName() + " - HP:" + (int) Math.round(hp) + "/" + Math.round(maxhp) + "  - "+ (int) Math.round((let.getLocation().distance(p.getLocation())))+" Blocks");
                    Double prog = ((hp / maxhp * 100) / 100);
                    if(prog>1){ prog= 1.0; }
                    bb1.setProgress(prog);
                }
                if (i == 2) {
                    double hp = let.getHealth();
                    double maxhp = let.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if(let.hasMetadata("fakehp")){
                        hp =  Math.round(((hp/2)-0.1)* 100.0) / 100.0;
                        maxhp =  Math.round((maxhp/2) * 100.0) / 100.0; }
                    bb2.setTitle(let.getName() + " - HP:" + (int) Math.round(hp) + "/" + Math.round(maxhp) + "  - "+ (int) Math.round((let.getLocation().distance(p.getLocation())))+" Blocks");
                    Double prog = ((hp / maxhp * 100) / 100);
                    if(prog>1){ prog= 1.0; }
                    bb2.setProgress(prog);
                }
                if (i == 3) {
                    double hp = let.getHealth();
                    double maxhp = let.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if(let.hasMetadata("fakehp")){
                        hp =  Math.round(((hp/2)-0.1)* 100.0) / 100.0;
                        maxhp =  Math.round((maxhp/2) * 100.0) / 100.0; }
                    bb3.setTitle(let.getName() + " - HP:" + (int) Math.round(hp) + "/" + Math.round(maxhp) + "  - "+ (int) Math.round((let.getLocation().distance(p.getLocation())))+" Blocks");
                    Double prog = ((hp / maxhp * 100) / 100);
                    if(prog>1){ prog= 1.0; }
                    bb3.setProgress(prog);
                }
                if (i == 4) {
                    double hp = let.getHealth();
                    double maxhp = let.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if(let.hasMetadata("fakehp")){
                        hp =  Math.round(((hp/2)-0.1)* 100.0) / 100.0;
                        maxhp =  Math.round((maxhp/2) * 100.0) / 100.0; }
                    bb4.setTitle(let.getName() + " - HP:" + (int) Math.round(hp) + "/" + Math.round(maxhp) + "  - "+ (int) Math.round((let.getLocation().distance(p.getLocation())))+" Blocks");
                    Double prog = ((hp / maxhp * 100) / 100);
                    if(prog>1){ prog= 1.0; }
                    bb4.setProgress(prog);
                }
                if (i == 5) {
                    double hp = let.getHealth();
                    double maxhp = let.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if(let.hasMetadata("fakehp")){
                        hp =  Math.round(((hp/2)-0.1)* 100.0) / 100.0;
                        maxhp =  Math.round((maxhp/2) * 100.0) / 100.0; }
                    bb5.setTitle(let.getName() + " - HP:" + (int) Math.round(hp) + "/" + Math.round(maxhp) + "  - "+ (int) Math.round((let.getLocation().distance(p.getLocation())))+" Blocks");
                    Double prog = ((hp / maxhp * 100) / 100);
                    if(prog>1){ prog= 1.0; }
                    bb5.setProgress(prog);
                }
            }
        }
    }
}
