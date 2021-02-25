package xyz.userghost.peacemaker.module;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.userghost.peacemaker.utils.Util;

import java.util.ArrayList;

public class Currency {
    public Currency(){

    }
    //== OverLoading Function ==//
    public ItemStack peace_coin(int level, int brp){
        Material mt = Material.GOLD_NUGGET;
        String ex = "§6Common";
        int cmd = 10031;
        if(level==1){
            mt = Material.GOLD_NUGGET;
            ex = "§bRare";
            cmd = 10032;
        }else if(level==2){
            mt = Material.GOLD_NUGGET;
            ex = "§aLegendary";
            cmd = 10033;
        }
        ItemStack peace_coins = new ItemStack(mt,brp);
        ItemMeta meta = peace_coins.getItemMeta();
        meta.addEnchant(Enchantment.LURE,1,false);
        ArrayList<String> lore = new ArrayList<String>();

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(subversion()>13) meta.setCustomModelData(cmd);
        lore.add(ChatColor.COLOR_CHAR+"7This Coins is the main currency which is");
        lore.add(ChatColor.COLOR_CHAR+"7used to purchase various items in PeaceMaker Kingdom.");
        lore.add(ChatColor.COLOR_CHAR+"7None disputes its value and status.");
        lore.add(ChatColor.COLOR_CHAR+"7It's said these coins existed before this kingdom was built.");
        meta.setLore(lore);
        meta.setDisplayName(ex+" PM Coins");
        peace_coins.setItemMeta(meta);
        return peace_coins;
    }
    public int subversion(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        version = version.replace("1_","X").replace("_R","Y");
        int subVersion = Integer.parseInt(version.substring(version.indexOf("X")+1,version.indexOf("Y")));
        return subVersion;
    }
}
