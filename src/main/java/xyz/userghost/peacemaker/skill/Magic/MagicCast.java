package xyz.userghost.peacemaker.skill.Magic;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MagicCast {
    int idtmr;
    Plugin my;
    Player player;
    public MagicCast(Plugin x,Player p,final int tmr,int countdown,final Runnable runnable){
        my = x;
        my.getServer().getScheduler().scheduleSyncDelayedTask(my, new Runnable(){
            public void run(){
                if(tmr!=0)
                    my.getServer().getScheduler().cancelTask(tmr);
                runnable.run();
            }},20*countdown);
    }

}
