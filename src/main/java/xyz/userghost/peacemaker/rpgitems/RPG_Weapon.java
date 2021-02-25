package xyz.userghost.peacemaker.rpgitems;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import xyz.userghost.peacemaker.utils.Util;

import java.util.ArrayList;

public class RPG_Weapon {
    Plugin my;
    Util util;
    public RPG_Weapon(Plugin x){
        my=x;
        util = new Util(my);
    }

    //Common -  abu abu
    //Uncommon - hijau
    //Rare - Biru
    //Very Rare - Ungu
    //Legendary - Gold
    //Mythic - Merah
    public ItemStack excalibur(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.DIAMOND_SWORD,count);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL,100,true);
        meta.addEnchant(Enchantment.KNOCKBACK,3,true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE,20,true);
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"[Rarity : &Mythic]"));
        lore.add(ChatColor.COLOR_CHAR+"athe legendary sword of King Arthur, sometimes also");
        lore.add(ChatColor.COLOR_CHAR+"aattributed with magical powers or associated");
        lore.add(ChatColor.COLOR_CHAR+"awith the rightful sovereignty of Britain.");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName("§4§kXX§r §bExcalibur §r§4§kXX§r");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }
    public ItemStack dragon_armor_helmet(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.DIAMOND_HELMET,count);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,20,true);
        meta.addEnchant(Enchantment.PROTECTION_PROJECTILE,20,true);
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS,20,true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE,20,true);
        meta.addEnchant(Enchantment.KNOCKBACK,3,true);
        meta.addEnchant(Enchantment.THORNS,5,true);
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        //if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.COLOR_CHAR+"aMaster Chief Helmet");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName("§bHalo Helmet");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }
    public ItemStack dragon_armor_chestplate(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.DIAMOND_CHESTPLATE,count);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,20,true);
        meta.addEnchant(Enchantment.PROTECTION_PROJECTILE,20,true);
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS,20,true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE,20,true);
        meta.addEnchant(Enchantment.KNOCKBACK,3,true);
        meta.addEnchant(Enchantment.THORNS,5,true);
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        //if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.COLOR_CHAR+"aMaster Chief Armor");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName("§bHalo Armor");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }
    public ItemStack dragon_armor_legging(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.DIAMOND_LEGGINGS,count);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,20,true);
        meta.addEnchant(Enchantment.PROTECTION_PROJECTILE,20,true);
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS,20,true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE,20,true);
        meta.addEnchant(Enchantment.KNOCKBACK,3,true);
        meta.addEnchant(Enchantment.THORNS,5,true);
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        //if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.COLOR_CHAR+"aMaster Chief Armor");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName("§bHalo Armor");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }
    public ItemStack dragon_armor_boots(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.DIAMOND_BOOTS,count);
        ItemMeta meta = is.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,20,true);
        meta.addEnchant(Enchantment.PROTECTION_PROJECTILE,20,true);
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS,20,true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE,20,true);
        meta.addEnchant(Enchantment.PROTECTION_FALL,30,true);
        meta.addEnchant(Enchantment.KNOCKBACK,3,true);
        meta.addEnchant(Enchantment.THORNS,5,true);
        meta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        //if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.COLOR_CHAR+"aMaster Chief Boots");
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName("§bHalo Boots");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }
    public ItemStack dragon_Shield(@Nullable int count){
        if(count==0) count = 1;
        ItemStack is = new ItemStack(Material.SHIELD,count);
        ItemMeta meta = is.getItemMeta();
        //if(util.subversion()>13) meta.setCustomModelData(10021);
        meta.setUnbreakable(true);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&aDragon Shield is most &cindestructible"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&ashield of the universe."));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a&a"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&aThis shield is made of dragon skin"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&aenchanted with adamantite."));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setDisplayName("§bDragon Shield");
        meta.setLore(lore);
        is.setItemMeta(meta);
        BlockStateMeta bmeta = (BlockStateMeta) is.getItemMeta();
        Banner banner = (Banner) bmeta.getBlockState();
        banner.setBaseColor(DyeColor.LIGHT_GRAY);
        banner.addPattern(new Pattern(DyeColor.GRAY, PatternType.STRIPE_CENTER));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.CROSS));
        banner.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.CIRCLE_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.GRAY, PatternType.FLOWER));
        banner.update();
        bmeta.setBlockState(banner);
        is.setItemMeta(bmeta);
        return is;
    }
}
