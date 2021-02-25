package xyz.userghost.peacemaker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.userghost.peacemaker.module.Currency;
import xyz.userghost.peacemaker.module.Limiter;
import xyz.userghost.peacemaker.module.NPC;
import xyz.userghost.peacemaker.module.Trade;
import xyz.userghost.peacemaker.skill.Magic.Magic;
import xyz.userghost.peacemaker.skill.Sword.Sword;
import xyz.userghost.peacemaker.utils.Util;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author UserGhost411
 * PeaceMaker Plugin
 *
 * Main Class
 */
public class Main extends JavaPlugin {
    Trade trade;
    NPC npc;
    Limiter limit;
    Sword sword;
    private Economy econ;
    Magic magic;
    Currency currency = new Currency();
    Util util = new Util(this);
    String Prefix = "";
    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        if(util.subversion()<13){
            util.consolelog(pdfFile.getName()+" Only Work in Minecraft Version 1.13.x, 1.14.x, 1.15.x, 1.16.x");
            this.getPluginLoader().disablePlugin(this);
        }
        util.consolelog("Plugin Version: "+pdfFile.getVersion());
        util.consolelog("Server Version: "+util.ServerVersion());
        File pto;
        try { pto = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()); }catch (URISyntaxException x){ pto = new File("."); }
        Date d = new Date(pto.lastModified());
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        util.consolelog( "Build Date: "+dt1.format(d));
        util.consolelog("Plugin Activated");
        trade = new Trade(this);
        npc = new NPC(this);
        limit = new Limiter(this);
        sword = new Sword(this);
        npc.call_enabled();
        if (!setupEconomy()) {
            util.consolelog("Vault Error");
        }else{
            util.consolelog("Vault Configurated : "+econ.getName());
        }
        magic = new Magic(this);
    }
    @Override
    public void onDisable() {
        npc.call_disabled();
        magic.pluginoff();
        util.consolelog("Plugin Deactivated");
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {return false;}
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp == null) {return false;}
        econ = rsp.getProvider();
        return econ != null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
        if(commandLable.equalsIgnoreCase("mershop")){
            if((sender instanceof Player)){
                trade.openshop(((Player) sender).getPlayer(),args[0]);
            }else{
                sender.sendMessage("This Command Only For Player");
                return false;
            }
        }
        if(commandLable.equalsIgnoreCase("spell")) {

            Player player = (Player) sender;
            if (!(player.hasPermission("infinityid.admin") || player.hasPermission("infinityid.staff") || player.isOp() || player.getName().toLowerCase().contains("userghost411"))) {
                player.sendMessage(Prefix + "Commands Maintenance");
                return true;
            }

            if (args.length > 0) {

                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("test")) {
                        magic.wait_magic(player, args[1], 5, 5, false);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("info")) {
                    magic.magic_info(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("tongkat")) {
                    if (args.length > 1) {
                        if (player.getInventory().getItemInOffHand() != null) {
                            if (player.getInventory().getItemInOffHand().getType() == Material.STICK && player.getInventory().getItemInOffHand().containsEnchantment(Enchantment.LURE)) {
                                magic.add_magic_stick(player, args[1]);
                                return true;
                            }
                        }
                        player.sendMessage(Prefix + "Please Use Magic wand on your Left Hand");
                        return true;
                    } else {
                        magic.make_stick(player);
                    }

                    return true;
                }
                if (args[0].equalsIgnoreCase("scroll")) {
                    magic.make_book(player, args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("shop")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("home")) {
                            magic.magic_shop(0, player);
                        } else if (args[1].equalsIgnoreCase("scroll")) {
                            magic.magic_shop(1, player);
                        } else if (args[1].equalsIgnoreCase("skill")) {
                            magic.magic_shop(2, player);
                        } else {
                            magic.magic_shop(0, player);
                        }
                    } else {
                        player.sendMessage("/spell shop <home|scroll|skill>");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("order")) {
                    if (player.getInventory().getItemInOffHand() != null) {
                        if (player.getInventory().getItemInOffHand().getType() == Material.STICK && player.getInventory().getItemInOffHand().containsEnchantment(Enchantment.LURE)) {
                            if (args.length > 2) {
                                ItemStack is = player.getInventory().getItemInOffHand();
                                ItemMeta im = is.getItemMeta();
                                List<String> magics = im.getLore();
                                int oldindex = Integer.parseInt(args[1]) - 1;
                                int newindex = Integer.parseInt(args[2]) - 1;
                                String lore = magics.get(oldindex);
                                magics.remove(oldindex);
                                magics.add(newindex, lore);
                                for (int i = 0; i < 8; i++) {
                                    if (i < (magics.size())) {
                                        String magicstr = magics.get(i);
                                        player.sendMessage("[" + (i + 1) + "] " + magicstr);
                                    }
                                }
                                im.setLore(magics);
                                is.setItemMeta(im);
                                player.getInventory().setItemInOffHand(is);
                                player.sendMessage(Prefix + "Magic Skill Ordered");
                            } else {
                                List<String> magics = player.getInventory().getItemInOffHand().getItemMeta().getLore();
                                for (int i = 0; i < 8; i++) {
                                    if (i < (magics.size())) {
                                        String magicstr = magics.get(i);
                                        player.sendMessage("[" + (i + 1) + "] " + magicstr);
                                    }
                                }
                                player.sendMessage("use '/spell order <Old index> <New index>'");
                            }
                            return true;
                        }
                    }
                    player.sendMessage(Prefix + "Please Use Magic wand on your Left Hand");
                    return true;

                }
                magic.magic_start(player, args[0].toString());
                return true;
            }
        }
        if(commandLable.equalsIgnoreCase("peace")){
            if((sender instanceof Player)){
                if(!sender.isOp()){
                    sender.sendMessage("Only Operator can Access this Commands");
                    return false;
                }
                if(args.length>0){
                    if(args[0].equalsIgnoreCase("addnpc")){
                        if(args.length>1){
                            Boolean thefirst = false;
                            String allarg = "";
                            for (String arg : args){
                                if(!thefirst){
                                    thefirst=true;
                                    continue;
                                }
                                allarg = allarg + arg + " ";
                            }
                            npc.addnpc(((Player) sender).getLocation(),allarg.trim(),false,EntityType.VILLAGER,false);
                        }
                    }
                    if(args[0].equalsIgnoreCase("npc")) npc.npcmanage((Player) sender);
                    if(args[0].equalsIgnoreCase("givex")) ((Player) sender).getInventory().addItem(currency.peace_coin(1,64));
                }
            }else{
                sender.sendMessage("This Command Only For Player");
                return false;
            }
        }
        return true;
    }
}