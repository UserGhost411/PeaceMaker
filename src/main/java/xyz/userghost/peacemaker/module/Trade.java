package xyz.userghost.peacemaker.module;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import xyz.userghost.peacemaker.rpgitems.RPG_Weapon;
import xyz.userghost.peacemaker.utils.Util;
import java.util.ArrayList;
import java.util.List;


public class Trade implements Listener {
    Plugin my;
    Currency currency = new Currency();
    Util util;
    public Trade(Plugin x) {
        my = x;
        
        util = new Util(my);
        Bukkit.getServer().getPluginManager().registerEvents(this,my);
        util.consolelog("TRADE Class Loaded");
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().getKiller() instanceof Player) {
            Entity en = event.getEntity();
            ItemStack is = new ItemStack(Material.AIR);
            int lv = 0;
            if(en instanceof Boss){
                int i = (int)Math.round((Math.random()+10) * 20);
                is = currency.peace_coin(2,i);
                en.getWorld().dropItemNaturally(en.getLocation(),is);
                return;
            }
            int i = (int)Math.round(Math.random() * 3);
            is = currency.peace_coin(lv,i);

            if (en.getType() == EntityType.ZOMBIE || en.getType() == EntityType.CREEPER || en.getType() == EntityType.SKELETON ||
                    en.getType() == EntityType.SPIDER ||en.getType() == EntityType.SLIME|| en.getType() == EntityType.PHANTOM ||
                    en.getType() == EntityType.MAGMA_CUBE ||en.getType() == EntityType.ZOMBIE_VILLAGER){
                if(is==null || is.getType()==Material.AIR || i == 0) return;
                en.getWorld().dropItemNaturally(en.getLocation(),is);
            }
            i = (int)Math.round(Math.random() * 10);
            if(i>5){
                i = (int)Math.round(Math.random() * 5);;
                lv = 1;
            }
            is = currency.peace_coin(lv,i);
            if (en.getType() == EntityType.PIG_ZOMBIE || en.getType() == EntityType.ENDERMAN || en.getType() == EntityType.CAVE_SPIDER|| en.getType() == EntityType.BLAZE
                    || en.getType() == EntityType.ELDER_GUARDIAN|| en.getType() == EntityType.GHAST|| en.getType() == EntityType.WITHER|| en.getType() == EntityType.WITHER_SKULL
                    || en.getType() == EntityType.SHULKER || en.getType() == EntityType.EVOKER|| en.getType() == EntityType.WITHER_SKELETON|| en.getType() == EntityType.PILLAGER){
                if(is==null || is.getType()==Material.AIR || i == 0) return;
                en.getWorld().dropItemNaturally(en.getLocation(),is);
            }
        }
    }
    public void openshop(Player player,String act) {
        Merchant merchant = Bukkit.createMerchant("PeaceMaker Shop");
        my.getLogger().info(act);
        if(act.equalsIgnoreCase("blacksmith")){
            merchant.setRecipes(blacksmith_recipe());
        }else if(act.equalsIgnoreCase("currency")){
            merchant.setRecipes(currency_recipe());
        }else if(act.equalsIgnoreCase("food")){
            merchant.setRecipes(food_recipe());
        }else if(act.equalsIgnoreCase("loot")){
            merchant.setRecipes(loot_recipe());
        }else{
            merchant.setRecipes(currency_recipe());
        }
        player.incrementStatistic(Statistic.TALKED_TO_VILLAGER);
        player.openMerchant(merchant, true);
    }
    //== OverLoading Function ==//
    private MerchantRecipe createMerchantRecipe(ItemStack item, ItemStack sellingItem, ItemStack item2) {
        MerchantRecipe recipe = new MerchantRecipe(sellingItem, 10000);
        recipe.setExperienceReward(false);
        recipe.addIngredient(item);
        recipe.addIngredient(item2);
        return recipe;
    }
    private MerchantRecipe createMerchantRecipe(ItemStack item, ItemStack sellingItem) {
        MerchantRecipe recipe = new MerchantRecipe(sellingItem, 10000);
        recipe.setExperienceReward(false);
        recipe.addIngredient(item);
        return recipe;
    }
    
    private List<MerchantRecipe> currency_recipe(){
        List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,16),currency.peace_coin(2,1)));

        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.GOLD_NUGGET,9),currency.peace_coin(0,1)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.GOLD_INGOT,1),currency.peace_coin(0,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,16),currency.peace_coin(1,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,16),currency.peace_coin(2,1)));
        //=======================
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.GOLD_NUGGET,9)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.GOLD_INGOT,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,1),currency.peace_coin(0,16)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,1),currency.peace_coin(1,16)));

        return merchantRecipes;
    }
    private List<MerchantRecipe> food_recipe(){
        List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,1),new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,10),new ItemStack(Material.GOLDEN_APPLE,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,4),new ItemStack(Material.SUSPICIOUS_STEW,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,3),new ItemStack(Material.MILK_BUCKET,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,3),new ItemStack(Material.CAKE,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,2),new ItemStack(Material.APPLE,1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,2),new ItemStack(Material.COOKED_BEEF,16)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_SALMON,8)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_PORKCHOP,8)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_MUTTON,8)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.BAKED_POTATO,16)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_CHICKEN,16)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_RABBIT,16)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,1),new ItemStack(Material.COOKED_COD,16)));
        return merchantRecipes;
    }
    private List<MerchantRecipe> loot_recipe(){
        List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.ARROW,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.STRING,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.SPIDER_EYE,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.BONE,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.ROTTEN_FLESH,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.GUNPOWDER,64),currency.peace_coin(0,5)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.DIRT,64),currency.peace_coin(0,1)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.COBBLESTONE,64),currency.peace_coin(0,2)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.DIORITE,64),currency.peace_coin(0,3)));
        merchantRecipes.add(createMerchantRecipe(new ItemStack(Material.GRANITE,64),currency.peace_coin(0,3)));
        return merchantRecipes;
    }
    private List<MerchantRecipe> blacksmith_recipe(){
        RPG_Weapon rpg_weap = new RPG_Weapon(my);
        List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();

        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,5),rpg_weap.excalibur(1),new ItemStack(Material.DIAMOND_SWORD)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,5),rpg_weap.dragon_Shield(1),new ItemStack(Material.SHIELD)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,4),rpg_weap.dragon_armor_legging(1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,3),rpg_weap.dragon_armor_helmet(1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,4),rpg_weap.dragon_armor_chestplate(1)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(2,3),rpg_weap.dragon_armor_boots(1)));


        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,2),new ItemStack(Material.DIAMOND_HELMET)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,3),new ItemStack(Material.DIAMOND_CHESTPLATE)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,2),new ItemStack(Material.DIAMOND_LEGGINGS)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,2),new ItemStack(Material.DIAMOND_BOOTS)));

        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(1,1),new ItemStack(Material.DIAMOND_SWORD)));

        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,8),new ItemStack(Material.SHIELD)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,7),new ItemStack(Material.IRON_LEGGINGS)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,7),new ItemStack(Material.IRON_HELMET)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,8),new ItemStack(Material.IRON_CHESTPLATE)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,7),new ItemStack(Material.IRON_BOOTS)));
        merchantRecipes.add(createMerchantRecipe(currency.peace_coin(0,6),new ItemStack(Material.IRON_SWORD)));
        return merchantRecipes;
    }
}
