package xyz.userghost.peacemaker.skill.Magic;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.userghost.peacemaker.module.Currency;
import xyz.userghost.peacemaker.utils.Util;

import java.io.File;
import java.util.*;

/**
 * @author UserGhost411
 * InfinityID Plugin
 *
 * Utilities Command Class
 */

public class Magic implements Listener {
    String Prefix = "";
    HashMap<String, Long> cooldownmagic = new HashMap<String,Long>();
    HashMap<String, Integer> silent = new HashMap<String,Integer>();
    HashMap<String, hotbar_data> hotbar_saved = new HashMap<String,hotbar_data>();
    ArrayList<MagicData> magicdata = new ArrayList<MagicData>();
    Material m_blank; 
    Plugin my;
    Util util = null;
    Summoner summon = null;
    Currency currency = new Currency();
    File configFile;
    FileConfiguration config;
    Boolean ismagic = false;
    ItemStack harga_magic_staff;
    List<String> forbidmagicworlds = new ArrayList<String>();
    public Magic(Plugin x) {
        my = x;
        util = new Util(x);
        Prefix = ChatColor.translateAlternateColorCodes('&',"&8[&r&bMagic&r&8]&r ");
        if(util.subversion()==12){
            m_blank = Material.valueOf("STAINED_GLASS_PANE");
        }else{
            m_blank = Material.BLACK_STAINED_GLASS_PANE;
        }
        Bukkit.getServer().getPluginManager().registerEvents(this,my);
        summon = new Summoner(x);
        loadmagic();
        configFile = new File(my.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            util.copy(my.getResource("config.yml"), configFile, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
            ismagic = config.getBoolean("magic.enabled");
            forbidmagicworlds = config.getStringList("magic.forbiddenworld");
        } catch (Exception e) { e.printStackTrace(); }
        harga_magic_staff = currency.peace_coin(1,10);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            Inventory inventory = event.getInventory();
            InventoryView invenview = event.getView();
            OfflinePlayer player1 = player;

            if(player.getInventory().getItemInOffHand().getType()==Material.STICK && (hotbar_saved.containsKey(player.getUniqueId().toString()))) {
                if(player.getInventory().getItemInOffHand().getItemMeta().hasEnchant(Enchantment.LURE)){
                    event.setCancelled(true);
                }
            }
            if (invenview.getTitle().equals("§9GraspHeart")) {
                if(clicked!=null) {
                    if(clicked.getType() != Material.AIR && clicked.getType() != m_blank) {
                        UUID uidnya = UUID.fromString(clicked.getItemMeta().getLore().get(0));
                        LivingEntity lol = (LivingEntity) player.getServer().getEntity(uidnya);
                        //player.sendMessage(uidnya.toString());
                        if (lol != null) {
                            lol.setHealth(0f);
                            if (lol instanceof Player)
                                lol.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bGrasp Hearth&r]&c You Killed by " + player.getDisplayName() + ""));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bGrasp Hearth&r]&b " + lol.getName() + "&r&a got killed by Grasp Hearth"));
                            player.closeInventory();
                        }
                    }
                }
                event.setCancelled(true);
            }
            if (invenview.getTitle().equals("§1§lMagic Shop")) {
                if(clicked==null) {
                    event.setCancelled(true);
                    return;
                }
                if(clicked.getType()==Material.BLAZE_POWDER){
                    magic_shop(2,player);
                }
                if(clicked.getType()==Material.PAPER){
                    magic_shop(1,player);
                }
                if(clicked.getType()==Material.STICK){
                    if(player.isOp()){
                        make_stick(player);
                        player.closeInventory();
                        event.setCancelled(true);
                        return;
                    }

                    if (player.getInventory().containsAtLeast(harga_magic_staff,harga_magic_staff.getAmount())) {
                        player.getInventory().remove(harga_magic_staff);
                        make_stick(player);
                        player.sendMessage(Prefix + "§aPurchase success ");
                    } else {
                        player.getPlayer().sendMessage(Prefix + "§cyou dont have enough money");
                    }
                    player.closeInventory();
                }
                if(clicked.getType()==Material.BARRIER){
                    magic_shop(0,player);
                }
                event.setCancelled(true);
            }
            if (invenview.getTitle().equals("§1§lMagic Shop - Scroll")) {
                if(clicked.getType()==Material.PAPER){
                   for (MagicData mdx : magicdata) {
                        if (clicked.getItemMeta().getDisplayName().contains(mdx.name)) {
                            if (player.getInventory().containsAtLeast(mdx.getPrice(),mdx.getPrice().getAmount())) {
                                player.getInventory().remove(mdx.getPrice());
                                make_book(player,mdx.getCmd());
                                player.sendMessage(Prefix + "§aPurchase success ");
                            } else {
                                player.getPlayer().sendMessage(Prefix + "§cyou dont have enough money");
                            }
                            break;
                        }
                    }
                    player.closeInventory();
                    return;
                }

                if(clicked.getType()==Material.BARRIER){
                    magic_shop(0,player);
                }
                event.setCancelled(true);
            }
            if (invenview.getTitle().equals("§1§lMagic Shop - Skill")) {
                if(clicked.getType()==Material.PAPER){
                    String magic = "";
                    Boolean found = false;
                    if(player.getInventory().getItemInOffHand().getType()==Material.STICK && player.getInventory().getItemInOffHand().containsEnchantment(Enchantment.LURE)){
                        for (MagicData mdx : magicdata) {
                            if (clicked.getItemMeta().getDisplayName().contains(mdx.name)) {
                                Boolean lol1 = false;
                                Boolean lol2 = false;
                                if (player.getInventory().getItemInOffHand().getItemMeta() != null && player.getInventory().getItemInOffHand().getItemMeta().getLore() != null){
                                    lol1 = player.getInventory().getItemInOffHand().getItemMeta().getLore().contains("§a" + mdx.getName() + "§1");
                                    lol2 = player.getInventory().getItemInOffHand().getItemMeta().getLore().contains("§a" + mdx.getCmd() + "§1");
                                }
                                if((!lol1) || (!lol2)){
                                    found = true;
                                    if (player.getInventory().containsAtLeast(mdx.getPrice(),mdx.getPrice().getAmount())) {
                                        player.getInventory().remove(mdx.getPrice());
                                        add_magic_stick(player,mdx.getCmd());
                                        player.sendMessage(Prefix + "§aPurchase success ");
                                    } else {
                                        player.getPlayer().sendMessage(Prefix + "§cyou dont have enough money");
                                    }
                                        break;
                                }else{
                                    found = true;
                                    player.sendMessage(Prefix + "§cyour Magic Staff already have this Magic!");
                                }
                            }
                        }
                        if(!found)player.sendMessage(Prefix + "§cundefined magic ");
                    }else{
                        player.sendMessage(Prefix + "Please put your Magic Staff in left hand");
                    }
                    player.closeInventory();
                }
                if(clicked.getType()==Material.BARRIER) magic_shop(0,player);
                event.setCancelled(true);
            }

        }catch (Exception e){
            event.setCancelled(true);
            e.printStackTrace();
        }
    }
    @EventHandler
    public void onItemDrop (PlayerDropItemEvent e) {
        if(e.getItemDrop()==null) return;
        if(e.getItemDrop().getItemStack().getType()==Material.END_CRYSTAL){
            if(e.getItemDrop().getItemStack().getItemMeta().hasEnchant(Enchantment.LURE)){
                e.setCancelled(true);
            }
        }
        if(e.getItemDrop().getItemStack().getType()==Material.BARRIER && hotbar_saved.containsKey(e.getPlayer().getUniqueId().toString())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void magicselect(PlayerItemHeldEvent e) {
        try {

            if(e.getPlayer().getInventory().getItemInOffHand()==null) return;
            if (hotbar_saved.containsKey(e.getPlayer().getUniqueId().toString()) &&  (e.getPlayer().getInventory().getItemInOffHand().getType() == Material.STICK && e.getPlayer().getInventory().getItemInOffHand().getItemMeta().hasEnchant(Enchantment.LURE))) {
                //e.getPlayer().sendTitle("Casting magic Now", "5 sec", 3, 7, 3);
                Player p = e.getPlayer();
                e.getPlayer().getInventory().setHeldItemSlot(8);
                if(!ismagic){ p.sendMessage(Prefix+"§cMagic Disabled");return; }
                if(forbidmagicworlds.contains(p.getWorld().getName())){ p.sendMessage(Prefix+"Magic disallowed on this World");return; }
                if(p.getInventory().getItem(e.getNewSlot()).getType()==Material.END_CRYSTAL){
                    if(p.getInventory().getItem(e.getNewSlot()).getItemMeta().hasEnchant(Enchantment.LURE)){
                        String magicname = p.getInventory().getItem(e.getNewSlot()).getItemMeta().getDisplayName();
                        for (MagicData mdx : magicdata) {
                            if(ChatColor.stripColor(magicname).equalsIgnoreCase(ChatColor.stripColor(mdx.getCmd())) || (ChatColor.stripColor(magicname).equalsIgnoreCase(ChatColor.stripColor(mdx.getName())))){
                                if(p.getLevel()>= mdx.getMinlevel()) {
                                    wait_magic(p, mdx.getCmd().toLowerCase(), mdx.getCast(),mdx.getCooldown(),false); }else{ p.sendMessage(Prefix+"§cThis spell need Level "+ mdx.getMinlevel()+" or greater"); }
                                return;
                            }
                        }
                        p.sendMessage(Prefix+"§cUndefined Spell");
                    }
                }
            }
        }catch (Exception ex){

        }
    }
    @EventHandler
    public void onSwapItemInHand(PlayerSwapHandItemsEvent e) {
        if(hotbar_saved.containsKey(e.getPlayer().getUniqueId().toString())){
            if(e.getPlayer().getInventory().getHeldItemSlot()!=8){
                e.setCancelled(true);
                return;
            }
        }
       if(e.getOffHandItem().getType()==Material.STICK && e.getOffHandItem().getItemMeta().hasEnchant(Enchantment.LURE) && (!hotbar_saved.containsKey(e.getPlayer().getUniqueId().toString()))){
           if(e.getPlayer().getInventory().getHeldItemSlot()!=8){
               e.getPlayer().getInventory().setHeldItemSlot(8);
               e.getPlayer().sendMessage(Prefix +"Please Place your Magic Staff to Hotbar slot 9 (Last Slot)");
               e.setCancelled(true);
               return;
           }else {
               List<String> magics = e.getOffHandItem().getItemMeta().getLore();
               PlayerInventory pi = e.getPlayer().getInventory();
               hotbar_data hd = new hotbar_data(pi.getItem(0), pi.getItem(1), pi.getItem(2), pi.getItem(3), pi.getItem(4), pi.getItem(5), pi.getItem(6), pi.getItem(7));
               hotbar_saved.put(e.getPlayer().getUniqueId().toString(), hd);
               ArrayList<String> lore = new ArrayList<String>();
               ItemStack isx;
               ItemStack isb = new ItemStack(Material.BARRIER);
               ItemMeta imb = isb.getItemMeta();
               imb.setDisplayName("No Spell");
               isb.setItemMeta(imb);
               if(magics==null || magics.isEmpty()){
                   for (int i=0; i<8; i++)
                       pi.setItem(i, isb);
               }else{
                   for (int i=0; i<8; i++){
                       pi.setItem(i, isb);
                       if(i<(magics.size())){
                           isx = new ItemStack(Material.END_CRYSTAL);
                           lore.clear();
                           String magic = magics.get(i);
                           ItemMeta im = isx.getItemMeta();
                           im.setDisplayName(magic);
                           im.addEnchant(Enchantment.LURE,1,true);
                           im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                           im.setLocalizedName(magic);
                           lore.add("§9Key Action Slot:§r§b"+(i+1));
                           for (MagicData mdx : magicdata) {
                               if ((ChatColor.stripColor(magic).trim().equalsIgnoreCase(ChatColor.stripColor(mdx.getCmd().toString()).trim()))||(ChatColor.stripColor(magic).trim().equalsIgnoreCase(ChatColor.stripColor(mdx.getName()).trim()))) {
                                   if(mdx.getDescription().contains("\n")){
                                       String lorespace[] = mdx.getDescription().split("\n");
                                       for (String loreloop : lorespace) { lore.add("§7"+ChatColor.translateAlternateColorCodes('&',loreloop)); }
                                   }else{
                                       lore.add("§7"+ChatColor.translateAlternateColorCodes('&',mdx.getDescription()));
                                   }
                                   lore.add("§1");
                                   lore.add("Cast time : "+mdx.getCast()+" Second");
                                   lore.add("Cooldown : "+mdx.getCooldown()+" Second");
                                   lore.add("Min level : "+mdx.getMinlevel()+"");
                                   im.setLore(lore);
                               }
                           }
                           im.setLore(lore);
                           isx.setItemMeta(im);
                           pi.setItem(i, isx);
                       }
                   }
               }
           }
       }
       if(e.getPlayer().getInventory().getHeldItemSlot()==8 && e.getMainHandItem().getType()==Material.STICK && e.getMainHandItem().getItemMeta().hasEnchant(Enchantment.LURE) && (hotbar_saved.containsKey(e.getPlayer().getUniqueId().toString()))) {
               PlayerInventory pi = e.getPlayer().getInventory();
               hotbar_data hd = hotbar_saved.get(e.getPlayer().getUniqueId().toString());
               pi.setItem(0,hd.getIs_1());
               pi.setItem(1,hd.getIs_2());
               pi.setItem(2,hd.getIs_3());
               pi.setItem(3,hd.getIs_4());
               pi.setItem(4,hd.getIs_5());
               pi.setItem(5,hd.getIs_6());
               pi.setItem(6,hd.getIs_7());
               pi.setItem(7,hd.getIs_8());
               hotbar_saved.remove(e.getPlayer().getUniqueId().toString());
       }
    }
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event){
        try {

            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getEnchants().containsKey(Enchantment.LURE)) {
                parse_book(event.getPlayer(), event);
            }
        }catch (Exception e){ }
    }
    @EventHandler
    public void oninvopen(InventoryOpenEvent event) {
        PlayerInventory pi = event.getPlayer().getInventory();
        if(hotbar_saved.containsKey(event.getPlayer().getUniqueId().toString()) && pi.getItemInOffHand().getType()==Material.STICK) {
            hotbar_data hd = hotbar_saved.get(event.getPlayer().getUniqueId().toString());
            pi.setItem(0,hd.getIs_1());
            pi.setItem(1,hd.getIs_2());
            pi.setItem(2,hd.getIs_3());
            pi.setItem(3,hd.getIs_4());
            pi.setItem(4,hd.getIs_5());
            pi.setItem(5,hd.getIs_6());
            pi.setItem(6,hd.getIs_7());
            pi.setItem(7,hd.getIs_8());
            hotbar_saved.remove(event.getPlayer().getUniqueId().toString());
        }
    }
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        PlayerInventory pi = event.getPlayer().getInventory();
        if((hotbar_saved.containsKey(event.getPlayer().getUniqueId().toString()))) {
            hotbar_data hd = hotbar_saved.get(event.getPlayer().getUniqueId().toString());
            pi.setItem(0,hd.getIs_1());
            pi.setItem(1,hd.getIs_2());
            pi.setItem(2,hd.getIs_3());
            pi.setItem(3,hd.getIs_4());
            pi.setItem(4,hd.getIs_5());
            pi.setItem(5,hd.getIs_6());
            pi.setItem(6,hd.getIs_7());
            pi.setItem(7,hd.getIs_8());
            hotbar_saved.remove(event.getPlayer().getUniqueId().toString());
        }

    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(!event.getKeepInventory()){
        PlayerInventory pi = event.getEntity().getInventory();
        if((hotbar_saved.containsKey(event.getEntity().getUniqueId().toString()))) {
            hotbar_data hd = hotbar_saved.get(event.getEntity().getUniqueId().toString());
            if(hd.getIs_1()!=null){
                if(hd.getIs_1().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_1());
            }
            if(hd.getIs_2()!=null) {
                if (hd.getIs_2().getType() != Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), hd.getIs_2());
            }
            if(hd.getIs_3()!=null){
                if(hd.getIs_3().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_3());
            }
            if(hd.getIs_4()!=null){
                if(hd.getIs_4().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_4());
            }
            if(hd.getIs_5()!=null){
                if(hd.getIs_5().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_5());
            }
            if(hd.getIs_6()!=null){
                if(hd.getIs_6().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_6());
            }
            if(hd.getIs_7()!=null){
                if(hd.getIs_7().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_7());
            }
            if(hd.getIs_8()!=null){
                if(hd.getIs_8().getType()!=Material.AIR)
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),hd.getIs_8());
            }
            hotbar_saved.remove(event.getEntity().getUniqueId().toString());
        }
            List<ItemStack> ehem = event.getDrops();
            for(ItemStack is : ehem){
                if(is!=null){
                    if(!(is.getType()==Material.END_CRYSTAL && is.getItemMeta().hasEnchant(Enchantment.LURE))){
                        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(),is);
                    }
                }
            }
            event.getDrops().clear();
        }
    }
    public void pluginoff(){
        for(Player p :Bukkit.getOnlinePlayers()) {
            PlayerInventory pi = p.getInventory();
            if((hotbar_saved.containsKey(p.getUniqueId().toString()))) {
                hotbar_data hd = hotbar_saved.get(p.getUniqueId().toString());
                pi.setItem(0,hd.getIs_1());
                pi.setItem(1,hd.getIs_2());
                pi.setItem(2,hd.getIs_3());
                pi.setItem(3,hd.getIs_4());
                pi.setItem(4,hd.getIs_5());
                pi.setItem(5,hd.getIs_6());
                pi.setItem(6,hd.getIs_7());
                pi.setItem(7,hd.getIs_8());
                hotbar_saved.remove(p.getUniqueId().toString());
            }
        }
    }
    @EventHandler
    public void entitydibakar(EntityCombustEvent event) { handle_entintydibakar(event); }
    @EventHandler
    public void entitygetattack(EntityDamageByEntityEvent event) { handle_entintyattacking(event); }
    @EventHandler
    public void onguardtarget(EntityTargetLivingEntityEvent event) { handle_entintytargeting(event);}

    public void loadmagic(){

        //========================TIER 1==========================
        magicdata.add(new MagicData("md1","Heal","Heal","\n&l1st Tier\n&oA spell that add low health\n&oto caster.",
                1,10,10,currency.peace_coin(0,4),10,20));
        magicdata.add(new MagicData("md2","Arrow","Arrow","\n&l1st Tier\n&oA spell that allows the caster to shoot\n&oout an 5 arrow that will pierce a target.",
                2,20,10,currency.peace_coin(0,4),20,50));
        magicdata.add(new MagicData("md3","Guard","Guard","\n&l1st Tier\n&oA spell that allows the caster to\n&osummon 1 zombie-type as guard.",
                5,60,10,currency.peace_coin(0,4),30,50));
        magicdata.add(new MagicData("md4","Speed","Speed","\n&l1st Tier\n&oA spell used to increase the\n&ocaster's speed.",
                2,60,10,currency.peace_coin(0,4),35,60));
        magicdata.add(new MagicData("md5","Fly","Fly","\n&a&l2nd Tier\n&oA spell that gives the caster the\n&oability to fly for 1 Minute.",
                2,80,20,currency.peace_coin(0,4),80,150));
        magicdata.add(new MagicData("md6","Fireball","Fireball","\n&a&l2nd Tier\n&oA spell that allows the user to\n&oshoots out 3 fireball that hit its target.",
                2,30,20,currency.peace_coin(0,4),80,150));
        magicdata.add(new MagicData("md7","Armor","Armor","\n&2&l3th Tier\n&oA spell that raises the\n&ocaster's defense.",
                5,15,30,currency.peace_coin(0,4),200,300));
        magicdata.add(new MagicData("md8","MassHeal","MassHeal","\n&2&l3th Tier\n&oA spell that add low health\n&oto caster's and allies around caster's.",
                8,15,30,currency.peace_coin(0,4),200,300));
        magicdata.add(new MagicData("md9","RegHeal","Regeneration Heal","\n&2&l3th Tier\n&oA spell that provides generate healing over time\n&oto caster's and allies around caster's.",
                5,20,30,currency.peace_coin(0,4),200,300));
        magicdata.add(new MagicData("md10","GreaterGuard","Greater Guard","\n&2&l3th Tier\n&oA spell that allows the caster to\n&osummon 3 zombie-type as guard.",
                8,120,30,currency.peace_coin(0,4),300,600));
        magicdata.add(new MagicData("md11","MassFly","Mass Fly","\n&2&l3th Tier\n&oA spell that gives the caster's\n&oand 3 allies around caster's the\n&oability to fly for 1 Minute.",
                8,180,30,currency.peace_coin(0,4),300,600));
        magicdata.add(new MagicData("md12","Blind","Blind","\n&e&l4th Tier\n&oA spell that makes your opponent\n&otemporarily blind.",
                5,50,40,currency.peace_coin(0,4),600,800));
        magicdata.add(new MagicData("md13","GreaterFireball","Greater Fireball","\n&6&l5th Tier\n&oA spell that allows the user to\n&oshoots out 10 fireball that hit\n&oits target.",
                6,40,50,currency.peace_coin(0,4),700,900));
        magicdata.add(new MagicData("md14","Invisible","Invisible","\n&6&l5th Tier\n&oA spell used to make the caster's invisible\n&oto the naked eye for 1 minute, but\n&onot the equipments.",
                20,120,50,currency.peace_coin(0,4),700,900));
        magicdata.add(new MagicData("md15","Wall","Wall","\n&6&l5th Tier\n&oA spell that creates a 3x2 wall in front\n&oof caster's made of dirt.",
                1,40,50,currency.peace_coin(0,4),700,900));
        magicdata.add(new MagicData("md16","Lightning","Lightning","\n&1&l7th Tier\n&oA spell that directly struck out\n&oa lightning 5 block in front of caster's.",
                2,40,70,currency.peace_coin(0,4),1000,1500));
        magicdata.add(new MagicData("md17","UltimateGuard","Ultimate Guard","\n&1&l7th Tier\n&oA spell that allows the caster to summon 5\n&ozombie-type with leather armor as guard.",
                10,180,70,currency.peace_coin(0,4),1000,1500));
        magicdata.add(new MagicData("md18","GreaterLightning","Greater Lightning","\n&1&l8th Tier\n&oA spell that directly struck out\n&oa lightning 10 block in front of caster's.",
                5,50,80,currency.peace_coin(1,5),2000,3000));
        magicdata.add(new MagicData("md19","Dust","Dust of Appearance","\n&d&l8th Tier\n&oA spell allows the caster to\n&ovreveal invisible units.",
                3,60,80,currency.peace_coin(1,5),2000,3000));
        magicdata.add(new MagicData("md20","HPEssence","HP Essence","\n&d&l8th Tier\n&oA spell allows the caster to\n&oview the health values of an enemy.",
                6,60,80,currency.peace_coin(2,1),2000,3000));
        magicdata.add(new MagicData("md21","FalseHP","False HP","\n&5&l9th Tier\n&oThe spell gives the user\n&othe ability to fake his/her HP (affected HP Essence).",
                6,60,90,currency.peace_coin(2,4),3000,4000));
        magicdata.add(new MagicData("md22","UltimateLightning","Ultimate Lightning","\n&1&l9th Tier\n&oA spell that directly struck out\n&oa 3 lightning in front of caster's.",
                10,60,90,currency.peace_coin(2,4),4800,5000));
        magicdata.add(new MagicData("md23","TrueGuard","True Guard","\n&5&l9th Tier\n&oA spell that allows the caster\n&oto summon 10 zombie-type with\n&oiron armor as guard.",
                15,240,90,currency.peace_coin(2,6),5000,8000));
        magicdata.add(new MagicData("md24","GraspHeart","Grasp Heart","\n&5&l9th Tier\n&oInstant Kill single nearby Entity selected by caster.",
                5,120,90,currency.peace_coin(2,8),9000,10000));
        magicdata.add(new MagicData("md25","Silent","Silent","\n&4&l10th Tier\n&oA spell that remove casting\n&otime to all spell 3 times.",
                30,3600,100,currency.peace_coin(2,10),9000,12000));
    }
    public void magic_shop(int shoptype,Player p){
        ItemStack menu;
        ItemMeta metamenu;
        ArrayList<Integer> dataindex = new ArrayList<Integer>();
        ArrayList<ItemStack> datamagic = new ArrayList<ItemStack>();
        ArrayList<String> lore = new ArrayList<String>();
        if(shoptype==0) { //Homepage Shop
            Inventory inv = Bukkit.getServer().createInventory(null,(9*3), "§1§lMagic Shop");
            menu = new ItemStack(Material.STICK, 1);
            metamenu = menu.getItemMeta();
            if(util.subversion()>13) metamenu.setCustomModelData(10012);
            lore.clear();
            lore.add("§7");
            lore.add("§7A magical weapon that you can use");
            lore.add("§7in your left hand to bring out the");
            lore.add("§7magic you have learned");
            lore.add("§7");
            lore.add("§7§l(§c§l?§r§7§l)§r§7Type /spell <spellname> to use");
            lore.add("§7magic you already learned");
            lore.add("§7§l(§6§l!§r§7§l)§r§7You must hold it in your left hand");
            lore.add("§7if you want to use it.");
            lore.add("§7");
            lore.add("§bPrice : §7"+harga_magic_staff.getAmount()+" "+harga_magic_staff.getItemMeta().getDisplayName()+"§r§7$");
            metamenu.setLore(lore);
            metamenu.setDisplayName("§dBuy §r§bMagic Staff§0§0");
            metamenu.setLocalizedName("§dBuy §r§bMagic Staff§0§0");
            metamenu.addEnchant(Enchantment.LURE, 1, true);
            metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            metamenu.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            metamenu.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            menu.setItemMeta(metamenu);
            inv.setItem(11,menu);
            dataindex.add(11);
            menu = new ItemStack(Material.PAPER, 1);
            metamenu = menu.getItemMeta();
            lore.clear();
            lore.add("§7");
            lore.add("§7List of Scroll that you");
            lore.add("§7can buy in this shop");
            metamenu.setLore(lore);
            metamenu.setDisplayName("§dScroll List§0§1");
            metamenu.setLocalizedName("§dScroll List§0§1");
            metamenu.addEnchant(Enchantment.LURE, 1, true);
            metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            menu.setItemMeta(metamenu);
            inv.setItem(13,menu);
            dataindex.add(13);
            menu = new ItemStack(Material.BLAZE_POWDER, 1);
            metamenu = menu.getItemMeta();
            lore.clear();
            lore.add("§7");
            lore.add("§7List of skill(spell) that");
            lore.add("§7you can buy in this shop");
            lore.add("§7§l(§6§l!§r§7§l)§r§7Please put your Magic Staff in left hand");
            metamenu.setLore(lore);
            metamenu.setDisplayName("§dSkill List§0§2");
            metamenu.setLocalizedName("§dSkill List§0§2");
            metamenu.addEnchant(Enchantment.LURE, 1, true);
            metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            menu.setItemMeta(metamenu);
            inv.setItem(15,menu);
            dataindex.add(15);
            ItemStack empdat = new ItemStack(m_blank,1,(short)15);
            ItemMeta itmet =  empdat.getItemMeta();
            for(int x=0;x<(inv.getStorageContents().length);x++){
                if(dataindex.contains(x)){continue;}
                itmet.setDisplayName("§"+String.format("%02d", x).replace("0","x").substring(0,1)+"§"+String.format("%02d", x).substring(1));
                empdat.setItemMeta(itmet);
                inv.setItem(x,empdat); }
            dataindex.clear();
            p.openInventory(inv);
        }else if(shoptype==1){//shop scroll
            Inventory inv = Bukkit.getServer().createInventory(null,54, "§1§lMagic Shop - Scroll");
            for(MagicData mdx: magicdata){

                menu = new ItemStack(Material.PAPER, 1);
                metamenu = menu.getItemMeta();
                lore.clear();
                if(mdx.getDescription().contains("\n")){
                    String lorespace[] = ChatColor.translateAlternateColorCodes('&',mdx.getDescription()).split("\n");
                    for (String loreloop : lorespace) { lore.add("§7"+loreloop); }
                }else{
                    lore.add("§7"+ ChatColor.translateAlternateColorCodes('&',mdx.getDescription()));
                }
                //lore.add("XP Range : "+cmddata[4]+"-"+cmddata[5]);
                lore.add("Cast time : "+mdx.getCast()+" Second");
                lore.add("Cooldown : "+mdx.getCooldown()+" Second");
                lore.add("Min level : "+mdx.getMinlevel()+"");
                lore.add(mdx.getId());
                lore.add("§bPrice : §7"+mdx.getPrice().getAmount() + " " + mdx.getPrice().getItemMeta().getDisplayName() +"§r§7$");
                metamenu.setLore(lore);
                metamenu.setDisplayName("§1§k0§r Scroll of §r"+mdx.getName()+" §r§1§k0§r");
                metamenu.setLocalizedName("§1§k0§r Scroll of §r"+mdx.getName().replace("&","§")+" §r§1§k0§r");
                metamenu.addEnchant(Enchantment.LURE, 1, true);
                metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                menu.setItemMeta(metamenu);
                datamagic.add(menu);
            }
            int j = 0;
            for(ItemStack mx : datamagic) { inv.addItem(mx);dataindex.add(j);j++; }
            //==========FILL_SLOT_KOSONG===========
            ItemStack empdat = new ItemStack(m_blank,1,(short)15);
            ItemMeta itmet =  empdat.getItemMeta();
            for(int x=0;x<(inv.getStorageContents().length);x++){
                if(dataindex.contains(x)){continue;}
                itmet.setDisplayName("§"+String.format("%02d", x).replace("0","x").substring(0,1)+"§"+String.format("%02d", x).substring(1));
                empdat.setItemMeta(itmet);
                inv.setItem(x,empdat); }
            dataindex.clear();
            //==========FILL_SLOT_KOSONG===========
            ItemStack backbtn = new ItemStack(Material.BARRIER);
            ItemMeta backmeta =  backbtn.getItemMeta();
            backmeta.setDisplayName("§cBack");
            backbtn.setItemMeta(backmeta);
            inv.setItem(53,backbtn);
            p.openInventory(inv);
        }else if(shoptype==2){//shop skill
            Inventory inv = Bukkit.getServer().createInventory(null,54, "§1§lMagic Shop - Skill");
            for(MagicData mdx : magicdata){
                menu = new ItemStack(Material.PAPER, 1);
                metamenu = menu.getItemMeta();
                lore.clear();
                if(mdx.getDescription().contains("\n")){
                    String lorespace[] = mdx.getDescription(true).split("\n");
                    for (String loreloop : lorespace) { lore.add("§7"+loreloop); }
                }else{
                    lore.add("§7"+mdx.getDescription(true).toString());
                }
                lore.add("Cast time : "+mdx.getCast()+" Second");
                lore.add("Cooldown : "+mdx.getCooldown()+" Second");
                lore.add("Min level : "+mdx.getMinlevel()+"");
                lore.add("§7XP Range : "+mdx.getXp_min()+"-"+mdx.getXp_max());
                lore.add(mdx.getId());
                lore.add("§bPrice : §7"+mdx.getPrice().getAmount() + " " + mdx.getPrice().getItemMeta().getDisplayName() +"§r§7$");
                metamenu.setLore(lore);
                metamenu.setDisplayName("§1§k0§r Skill of §r"+mdx.getName()+" §r§1§k0§r");
                metamenu.setLocalizedName("§1§k0§r Skill of §r"+mdx.getName().replace("&","§")+" §r§1§k0§r");
                metamenu.addEnchant(Enchantment.LURE, 1, true);
                metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                menu.setItemMeta(metamenu);
                datamagic.add(menu);
            }
            int j = 0;
            for(ItemStack mx : datamagic) { inv.addItem(mx);dataindex.add(j);j++; }
            //==========FILL_SLOT_KOSONG===========
            ItemStack empdat = new ItemStack(m_blank,1,(short)15);
            ItemMeta itmet =  empdat.getItemMeta();
            for(int x=0;x<(inv.getStorageContents().length);x++){
                if(dataindex.contains(x)){continue;}
                itmet.setDisplayName("§"+String.format("%02d", x).replace("0","x").substring(0,1)+"§"+String.format("%02d", x).substring(1));
                empdat.setItemMeta(itmet);
                inv.setItem(x,empdat); }
            dataindex.clear();
            //==========FILL_SLOT_KOSONG===========
            ItemStack backbtn = new ItemStack(Material.BARRIER);
            ItemMeta backmeta =  backbtn.getItemMeta();
            backmeta.setDisplayName("§cBack");
            backbtn.setItemMeta(backmeta);
            inv.setItem(53,backbtn);
            p.openInventory(inv);
        }else{
            p.sendMessage(Prefix+"Sorry , i dont understand?");
            return;
        }
    }

    public void magic_info(Player p){
        ItemStack menu;
        ItemMeta metamenu;
        ArrayList<ItemStack> datamagic = new ArrayList<ItemStack>();
        Inventory inv = Bukkit.getServer().createInventory(null,54, "§1§lMagic Info");
        for(MagicData mdx : magicdata){
            menu = new ItemStack(Material.PAPER, 1);
            metamenu = menu.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            if(mdx.getDescription().contains("\n")){
                String lorespace[] = mdx.getDescription(true).split("\n");
                for (String loreloop : lorespace) {
                    lore.add("§7"+loreloop);
                }
            }else{
                lore.add("§7"+mdx.getDescription(true));
            }
            metamenu.setLore(lore);
            metamenu.setDisplayName("§1§k0§r §r"+mdx.getName()+" §r§1§k0§r");
            metamenu.setLocalizedName("§1§k0§r §r"+mdx.getName().replace("&","§")+" §r§1§k0§r");
            metamenu.addEnchant(Enchantment.LURE, 1, true);
            metamenu.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            menu.setItemMeta(metamenu);
            datamagic.add(menu);
        }
        int j = 0;
        for(ItemStack mx : datamagic) {
            inv.addItem(mx);
            j++;
        }
        ItemStack empdat = new ItemStack(m_blank,1,(short)15);
        ItemMeta itmet =  empdat.getItemMeta();

        for(int x=0;x<(inv.getStorageContents().length-j);x++){
            itmet.setDisplayName("§"+String.format("%02d", x).replace("0","x").substring(0,1)+"§"+String.format("%02d", x).substring(1));
            empdat.setItemMeta(itmet);
            inv.addItem(empdat);
        }
        p.openInventory(inv);
    }
    public void make_stick(Player p){

        ItemStack is = new ItemStack(Material.STICK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Magic Staff");
        if(util.subversion()>13) im.setCustomModelData(10012);
        ArrayList<String> lore = new ArrayList<String>();
        if(p.isOp()){
            for(MagicData mdx : magicdata){
                lore.add("§a"+mdx.getName()+"§1");
            }
        }
        im.setLore(lore);
        im.addEnchant(Enchantment.LURE, 1,true);
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        p.getInventory().addItem(is);
    }
    public void add_magic_stick(Player p,String magic){
        final MagicData mdx = search_md(magic);
        if(mdx!=null) {
            ItemStack is = p.getInventory().getItemInOffHand();
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("Magic Staff");
            List<String> lore = null;
            if (im.hasLore()) {
                lore = im.getLore();
                if (lore.contains("§a" + mdx.getName() + "§1")) {
                    p.sendMessage(Prefix + "Magic Staff already have this Magic!");
                    return;
                }
            } else {
                lore = new ArrayList<String>();
            }
            lore.add("§a" +  mdx.getName() + "§1");
            im.setLore(lore);
            im.addEnchant(Enchantment.LURE, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
            p.getInventory().setItemInOffHand(new ItemStack(Material.AIR, 1));
            p.getInventory().setItemInOffHand(is);
        }
    }

    public void make_book(Player p,String magic){

        int id1 = util.rand(1, 9);
        int id2 = util.rand(1, 9);
        int id3 = util.rand(1, 9);
        int id4 = util.rand(1, 9);
        ItemStack is = new ItemStack(Material.PAPER, 1);
        for(MagicData mdx : magicdata){
            if(magic.equalsIgnoreCase((mdx.getCmd().toString()))) {
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("§1§k0§r §bScroll of §r"+mdx.getName()+" §r§1§k0§r");
                ArrayList<String> lore = new ArrayList<String>();
                if(mdx.getDescription().contains("\n")){
                    String lorespace[] = mdx.getDescription(true).split("\n");
                    for (String loreloop : lorespace) {
                        lore.add("§7"+loreloop);
                    }
                }else{
                    lore.add("§7"+mdx.getDescription());
                }
                lore.add(mdx.getId());
                lore.add("§5Right click to Use | Single use scroll"+"§a§" + id1 + "§" + id2 + "§" + id3 + "§" + id4);
                im.setLore(lore);
                im.addEnchant(Enchantment.LURE, 1, true);
                im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                is.setItemMeta(im);
                p.getInventory().addItem(is);
                return;
            }
        }
    }
    public void parse_book(Player p, PlayerInteractEvent e){
        if(!ismagic){ p.sendMessage(Prefix+"§cMagic Disabled");return; }
        if(forbidmagicworlds.contains(p.getWorld().getName())){ p.sendMessage(Prefix+"Magic disallowed on this World");return; }
        for(MagicData mdx : magicdata){
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(mdx.getName())) {
                //p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                p.getInventory().remove(e.getPlayer().getInventory().getItemInMainHand());
                p.getInventory().setItemInMainHand(null);
                wait_magic(p, mdx.getCmd().toLowerCase(), mdx.getCast(), mdx.getCooldown(), true);
                return;
            }
        }
    }
    public void magic_start(Player p , String cmd){
        if(!ismagic){ p.sendMessage(Prefix+"§cMagic Disabled");return; }
        if(forbidmagicworlds.contains(p.getWorld().getName().toString())){ p.sendMessage(Prefix+"Magic disallowed on this World");return; }

        if(p.getInventory().getItemInOffHand()==null){ return ; }
        if(p.getInventory().getItemInOffHand().getType()==Material.STICK){
            if(p.getInventory().getItemInOffHand().containsEnchantment(Enchantment.LURE)){
                if(!p.getInventory().getItemInOffHand().getItemMeta().hasLore()){ p.sendMessage(Prefix+"§aMagic Staff §c is empty like your heart :'(");return; }
                ItemStack hehe = p.getInventory().getItemInOffHand();
                for(MagicData mdx : magicdata){
                    if((cmd.equalsIgnoreCase(mdx.getCmd()) && hehe.getItemMeta().getLore().contains("§a"+mdx.getCmd()+"§1")) || (cmd.equalsIgnoreCase(mdx.getName()) && hehe.getItemMeta().getLore().contains("§a"+mdx.getName()+"§1"))){
                        int minlvl = mdx.getMinlevel();
                        if(p.getLevel()>=minlvl) { wait_magic(p, mdx.getCmd().toLowerCase(), mdx.getCast(),mdx.getCooldown(),false); }else{ p.sendMessage(Prefix+"§cThis spell need Level "+minlvl+" or greater"); }
                        return;
                    }
                }
                p.sendMessage(Prefix+"§cUndefined Spell");
            }else{
                p.sendMessage(Prefix+"§cInvalid Magic Staff");
            }
        }else{
            p.sendMessage(Prefix+"§cPlease use §aMagic Staff");
        }

    }
    public void wait_magic(final Player p,final String magic,int sec,int cooldown,final Boolean byscrool){
        if(p.isOp()){ sec=0;cooldown=0; }
        if(silent.containsKey(p.getUniqueId().toString())){
            int silentint = (silent.get(p.getUniqueId().toString())-1);
            silent.put(p.getUniqueId().toString(),silentint);
            if(silentint==0){ silent.remove(p.getUniqueId().toString()); }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Prefix +"&aSilent Magic Left : &9"+silentint));
            run_magic(p,magic,byscrool);
            return;
        }
        if (!byscrool){// && !p.isOp()){
            if(get_cooldown(p,magic)==0) {
                set_cooldown(p, magic, cooldown);
            }else{
                p.sendMessage(Prefix+"Please Wait "+ get_cooldown(p,magic)+ " Seconds to use this magic again...");
                return;
            }}
        final int secfinal = sec;
        final MagicData mdx = search_md(magic);
        final int tmrid = new BukkitRunnable() {
            double alpha = 0;
            double alphay = 0;
            public void run() {
                if(alpha==360) alpha=0;
                alpha += 15;
                alphay += 0.05;
                if(alphay>3) alphay = 0;
                Location loc = p.getLocation();
                double rad = Math.toRadians(alpha);
                double cosine = Math.cos(rad);
                double sine = Math.sin(rad);
                Vector ve1 = new Vector((cosine * 1 - sine * 1),  alphay, (sine * 1 + cosine * 1));
                Vector ve2 = new Vector((cosine * 1 - sine * 1)*-1,  alphay, (sine * 1 + cosine * 1)*-1);
                Location firstLocation = loc.clone().add(ve1);
                Location firstLocation2 = loc.clone().add(ve2);
                for(Player x : Bukkit.getOnlinePlayers()){
                    x.spawnParticle( Particle.FLAME, firstLocation, 0, 0, 0, 0, 0 );
                    x.spawnParticle( Particle.FLAME, firstLocation2, 0, 0, 0, 0, 0 );
                }

            }
        }.runTaskTimer( my, 0, 1 ).getTaskId();
        new BukkitRunnable() {
            int i = secfinal;
            public void run(){
                if(i<=0){
                    run_magic(p,magic,byscrool);
                    Bukkit.getScheduler().cancelTask(tmrid);
                    this.cancel();
                }
                playeraction(p, ChatColor.translateAlternateColorCodes('&',"&c[&r"+i+"&c]&r &aCasting Spell &8[&r &b"+mdx.getName()+"&r &8]&r &r &c[&r"+i+"&c]&r"));
                i--;
            }
        }.runTaskTimer( my, 0, 20 );
    }
    public void run_magic(final Player p,String magic,Boolean byscrool){
        //====================TIER 1 MAGIC========================
        if(magic.equals("heal")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1,false,false));
        }
        if(magic.equals("arrow")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            new BukkitRunnable() {
                int i = 0;
                public void run(){
                    if(i>=5) this.cancel();
                    Arrow arrow = p.getWorld().spawn(p.getEyeLocation(),Arrow.class);
                    Vector finalVector = p.getLocation().getDirection().multiply(9.5D);
                    arrow.setVelocity(finalVector);
                    arrow.setShooter(p);
                    i++;
                }
            }.runTaskTimer( my, 20, 20 );
        }
        if(magic.equals("guard")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))*-1); }
            int max=0;
            int totalspawn = 3;
            while(max < totalspawn){
                for (Entity et : util.getentitas(p,10)) {
                    if (max >= totalspawn) break;
                    if (et instanceof LivingEntity) {
                        if( et instanceof Player) { if (et == p) continue; }
                        if (et.hasMetadata("cantburn")) continue;
                        LivingEntity living = (LivingEntity) et;
                        summon.zombiesummon(p, living, "low");
                        max++;
                    }
                }
            }
            playeraction(p, mdx.getName()+" Spawned");
        }
        if(magic.equals("speed")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (120*20), 1,false,false));
        }
        //====================TIER 2 MAGIC========================
        if(magic.equals("fly")){
            final MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            p.setAllowFlight(true);
            p.setMetadata("flymagic", new FixedMetadataValue(my, p.getUniqueId().toString()));
            MagicCast md = new MagicCast(my, p, 0,60, new Runnable() {
                @Override
                public void run() {
                    p.setAllowFlight(false);
                    if(p.hasMetadata("flymagic")){ p.removeMetadata("flymagic", my); }
                    playeraction(p, mdx.getName()+" Deactived");
                }
            });
        }
        if(magic.equals("fireball")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            new BukkitRunnable() {
                int i = 0;
                public void run(){
                    if(i>=5) this.cancel();
                    Fireball fireball = p.getWorld().spawn(p.getEyeLocation(),Fireball.class);
                    Vector velocity = fireball.getDirection();
                    fireball.setDirection(velocity.multiply(5.0));
                    fireball.setShooter(p);
                    i++;
                }
            }.runTaskTimer( my, 20, 20 );
        }

        //====================TIER 3 MAGIC========================
        if(magic.equals("armor")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            for (Player px : util.getPlayersWithin(p,5)) {
                playeraction(px,"Armor Actived");
                if(px.hasPotionEffect(PotionEffectType.ABSORPTION)){ px.removePotionEffect(PotionEffectType.ABSORPTION); }
                px.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60, 1));
            }
        }
        if(magic.equals("massheal")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            for (Player px : util.getPlayersWithin(p,5)) {
                playeraction(px, mdx.getName()+" Activated");
                px.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, (20), 1,false,false));
            }
        }
        if(magic.equals("regheal")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            for (Player px : util.getPlayersWithin(p,5)) {
                playeraction(px, mdx.getName()+" Activated");
                px.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (20*20), 2,false,false));
            }
        }
        if(magic.equals("greaterguard")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            int max=0;
            int totalspawn = 5;
            while(max < totalspawn){
                for (Entity et : util.getentitas(p,10)) {
                    if (max >= totalspawn) break;
                    if (et instanceof LivingEntity) {
                        if( et instanceof Player) { if (et == p) continue; }
                        if (et.hasMetadata("cantburn")) continue;
                        LivingEntity living = (LivingEntity) et;
                        summon.zombiesummon(p, living, "low");
                        max++;
                    }
                }
            }
            playeraction(p, mdx.getName()+" Spawned");
        }
        if(magic.equals("massfly")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            for (final Player px : util.getPlayersWithin(p,5)) {
                playeraction(px, mdx.getName()+" Actived");
                px.setAllowFlight(true);
                px.setMetadata("flymagic", new FixedMetadataValue(my, p.getUniqueId().toString()));
                MagicCast md = new MagicCast(my, px, 0,60, new Runnable() {
                    @Override
                    public void run() {
                        px.setAllowFlight(false);
                        if(px.hasMetadata("flymagic")){ p.removeMetadata("flymagic", my); }
                        playeraction(px, "Fly Deactived");
                    }
                });
            }
        }
        //====================TIER 4 MAGIC========================
        if(magic.equals("blind")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Activated");
            for (Player px : util.getPlayersWithin(p,10)) {
                if(px==p){continue;}
                px.playSound(px.getLocation(),Sound.ENTITY_ENDERMAN_STARE,1f,1f);
                px.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (20*20), 2,false,false));
            }
        }
        //====================TIER 5 MAGIC========================
        if(magic.equals("greaterfireball")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            new BukkitRunnable() {
                int i = 0;
                public void run(){
                    if(i>=10) this.cancel();
                    Fireball fireball = p.getWorld().spawn(p.getEyeLocation(),Fireball.class);
                    Vector velocity = fireball.getDirection();
                    fireball.setDirection(velocity.multiply(5.0));
                    fireball.setShooter(p);
                    i++;
                }
            }.runTaskTimer( my, 20, 20 );
        }
        if(magic.equals("invisible")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (20*60), 2,false,false));
        }
        if(magic.equals("wall")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            Location eyelocation = p.getLocation();
            Location frontlocation = null;
            Material mat = null;
            if(util.getCardinalDirection(p).equals("W")){
                mat = Material.STONE; //pmat = p.getLocation().add(2,-1,0).getBlock().getType();
                //if (mat == Material.COAL_ORE || mat == Material.IRON_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat == Material.LAPIS_ORE || mat == Material.QUARTZ_ORE || mat == Material.REDSTONE_ORE || mat == Material.DIAMOND_ORE){ mat = Material.DIRT; }
                frontlocation = eyelocation.add(2,0,0);
                summon.wallsummon(frontlocation,1,p,mat);
            }else  if(util.getCardinalDirection(p).equals("N")){
                mat = Material.STONE; //pmat = p.getLocation().add(2,-1,0).getBlock().getType();
                //if (mat == Material.COAL_ORE || mat == Material.IRON_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat == Material.LAPIS_ORE || mat == Material.QUARTZ_ORE || mat == Material.REDSTONE_ORE || mat == Material.DIAMOND_ORE){ mat = Material.DIRT; }
                frontlocation = eyelocation.add(0,0,2);
                summon.wallsummon(frontlocation,0,p,mat);
            }else  if(util.getCardinalDirection(p).equals("S")){
                mat = Material.STONE; //pmat = p.getLocation().add(2,-1,0).getBlock().getType();
                //if (mat == Material.COAL_ORE || mat == Material.IRON_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat == Material.LAPIS_ORE || mat == Material.QUARTZ_ORE || mat == Material.REDSTONE_ORE || mat == Material.DIAMOND_ORE){ mat = Material.DIRT; }
                frontlocation = eyelocation.add(0,0,-2);
                summon.wallsummon(frontlocation,0,p,mat);
            }else  if(util.getCardinalDirection(p).equals("E")) {
                mat = Material.STONE; //p.getLocation().add(2, -1, 0).getBlock().getType();
                //if (mat == Material.COAL_ORE || mat == Material.IRON_ORE || mat == Material.EMERALD_ORE || mat == Material.GOLD_ORE || mat == Material.LAPIS_ORE || mat == Material.QUARTZ_ORE || mat == Material.REDSTONE_ORE || mat == Material.DIAMOND_ORE){ mat = Material.DIRT; }
                frontlocation = eyelocation.add(-2,0,0);
                summon.wallsummon(frontlocation,1,p,mat);
            }
            //p.sendMessage();
        }
        //====================TIER 7 MAGIC========================
        if(magic.equals("lightning")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            ArrayList<Block> Blocks = (ArrayList<Block>) p.getPlayer().getLineOfSight((Set) null, 8);
            int sd=0;
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (20*2), 4,false,false));
            for (Block block : Blocks) {
                sd++;
                if(sd<4){continue;}
                Block bl = p.getWorld().getHighestBlockAt(block.getLocation());
                p.getWorld().strikeLightning(bl.getLocation());
            }
        }
        if(magic.equals("ultimateguard")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            int max=0;
            int totalspawn = 10;
            while(max < totalspawn){
                for (Entity et : util.getentitas(p,10)) {
                    if (max >= totalspawn) break;
                    if (et instanceof LivingEntity) {
                        if( et instanceof Player) { if (et == p) continue; }
                        if (et.hasMetadata("cantburn")) continue;
                        LivingEntity living = (LivingEntity) et;
                        summon.zombiesummon(p, living, "med");
                        max++;
                    }
                }
            }
            playeraction(p, mdx.getName()+" Spawned");
        }
        //====================TIER 8 MAGIC========================
        if(magic.equals("greaterlightning")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            ArrayList<Block> Blocks = (ArrayList<Block>) p.getPlayer().getLineOfSight((Set) null, 15);
            int sd=0;
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (20*2), 4,false,false));
            for (Block block : Blocks) {
                sd++;
                if(sd<4){continue;}
                Block bl = p.getWorld().getHighestBlockAt(block.getLocation());
                p.getWorld().strikeLightning(bl.getLocation());
            }
        }
        if(magic.equals("hpessence")){
            final MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Actived");
            p.setMetadata("hpessence", new FixedMetadataValue(my, p.getUniqueId().toString()));
            final BossBarData bbd = new BossBarData(p,util);
            int tmr = my.getServer().getScheduler().scheduleSyncRepeatingTask(my, new Runnable() {
                public void run() {
                    bbd.updatebb();
                }
            },10,10);
            MagicCast md = new MagicCast(my, p, tmr,60, new Runnable() {
                @Override
                public void run() {
                    if(p.hasMetadata("hpessence")){ p.removeMetadata("hpessence", my); }
                    playeraction(p, mdx.getName()+" Deactived");
                    bbd.closebb();
                }
            });

        }
        if(magic.equals("dust")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            int enx = 0;
            for (Entity et : util.getentitas(p,20)) {
                if(et==p){continue;}
                if(et!=null && et instanceof LivingEntity) {
                    LivingEntity liveEnt = (LivingEntity)et;
                    liveEnt.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (30 * 20), 1, false, false));
                enx++;
                }
            }
            playeraction(p, ChatColor.translateAlternateColorCodes('&',"Dust of Appearance &aActived&r , &b"+enx+" &cEntity Found"));
        }
        //====================TIER 9 MAGIC========================
        if(magic.equals("ultimatelightning")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p,"§a"+mdx.getName());
            int max = 0;
            for (Entity et : util.getentitas(p,10)) {
                if(et instanceof  LivingEntity) {
                    if (max == 3) break;
                    if(et == p) continue;
                    et.getWorld().strikeLightning(et.getLocation());
                    max++;
                }
            }
        }
        if(magic.equals("falsehp")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            playeraction(p, mdx.getName()+" Actived");
            p.setMetadata("fakehp", new FixedMetadataValue(my, p.getUniqueId().toString()));
            MagicCast md = new MagicCast(my, p, 0,60, new Runnable() {
                @Override
                public void run() {
                    if(p.hasMetadata("fakehp")){ p.removeMetadata("fakehp", my); }
                    playeraction(p, "False Data : HP Deactived");
                }
            });
        }
        if(magic.equals("trueguard")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            int max=0;
            int totalspawn = 20;
            while(max < totalspawn){
                for (Entity et : util.getentitas(p,10)) {
                    if (max >= totalspawn) break;
                    if (et instanceof LivingEntity) {
                        if( et instanceof Player) { if (et == p) continue; }
                        if (et.hasMetadata("cantburn")) continue;
                        LivingEntity living = (LivingEntity) et;
                        summon.zombiesummon(p, living, "high");
                        max++;
                    }
                }
            }
            playeraction(p, mdx.getName()+" Spawned");
        }
        if(magic.equals("graspheart")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))); }
            Inventory inv = p.getServer().createInventory(null,9*5, "§9GraspHeart");
            int idv = 0;
            for (Entity et : util.getentitas(p,20)) {
                if (et instanceof LivingEntity) {
                    ItemStack is =  new ItemStack(Material.PLAYER_HEAD);
                    if( et instanceof Player) {
                        if (et == p) continue;
                        is =  new ItemStack(Material.PLAYER_HEAD);
                    }else {
                        try {
                            is = new ItemStack(Material.getMaterial(et.getName().toUpperCase() + "_SPAWN_EGG"));
                        }catch (Exception e){
                            is =  new ItemStack(Material.EMERALD);
                        }
                        if(et.getType()==EntityType.ZOMBIE)
                            is =  new ItemStack(Material.ZOMBIE_HEAD);
                        if(et.getType()==EntityType.CREEPER)
                            is =  new ItemStack(Material.CREEPER_HEAD);
                        if(et.getType()==EntityType.SKELETON)
                            is =  new ItemStack(Material.SKELETON_SKULL);
                        if(et.getType()==EntityType.ENDER_DRAGON)
                            is =  new ItemStack(Material.DRAGON_HEAD);
                        if(et.getType()==EntityType.WITHER)
                            is =  new ItemStack(Material.WITHER_SKELETON_SKULL);
                        if(et.getType()==EntityType.WITHER_SKELETON)
                            is =  new ItemStack(Material.WITHER_SKELETON_SKULL);
                        if(et.getType()==EntityType.ENDERMAN)
                            is =  new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                    }
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(et.getName());
                    List<String> lolhehe = new ArrayList<String>();
                    lolhehe.add(et.getUniqueId().toString());
                    im.setLore(lolhehe);
                    is.setItemMeta(im);
                    inv.addItem(is);
                    idv++;
                }
            }
            while(idv != ((9*5))){
                inv.setItem(idv,new ItemStack(m_blank));
                idv++;
            }
            p.openInventory(inv);
            playeraction(p, mdx.getName());
        }
        //====================TIER 10 MAGIC========================
        if(magic.equals("silent")){
            MagicData mdx = search_md(magic);
            if(mdx==null) return;
            if (byscrool==false) { util.changePlayerExp(p, (util.rand(mdx.getXp_min(), mdx.getXp_max()))*-1); }
            silent.put(p.getUniqueId().toString(),3);
            playeraction(p, mdx.getName()+" Activated");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aSilent Magic Left : &93"));
        }
    }
    public void playeraction (Player p , String msg){ util.sendActionbar(p,msg); }
    public void set_cooldown(Player p,String magic,int cooldown){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,cooldown);
        Date dt = calendar.getTime();
        long epoch = dt.getTime();
        cooldownmagic.put(p.getUniqueId().toString()+"-"+magic, (long)(epoch/1000));
    }
    public int get_cooldown(Player p,String magic){
        if(cooldownmagic.containsKey((p.getUniqueId().toString()+"-"+magic))){
            long last = cooldownmagic.get((p.getUniqueId().toString()+"-"+magic));
            Date currentDate = new Date();
            long now = (currentDate.getTime() / 1000);
            if((last-now)<1){
                return 0;
            }else{
                return (int)(last-now);
            }
        }else{
            return 0;
        }
    }
    //============================EVENT HANDLE================================
    //event listener dimana entity mendamage sesuatu
    public void handle_entintyattacking(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player) {
            Player p = (((Player) event.getEntity()).getPlayer());
            for (Entity et : util.getentitas(p,20)) {
                if(et.hasMetadata(p.getUniqueId().toString())){
                    Zombie z = (Zombie) et;
                    if(event.getDamager() instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) event.getDamager();
                        z.setTarget(living);
                    }
                }
            }
        }
        if(event.getDamager() instanceof Player) {
            Player p = (((Player) event.getDamager()).getPlayer());
            for (Entity et : util.getentitas(p,20)) {
                if(et.hasMetadata(p.getUniqueId().toString())){
                    Zombie z = (Zombie) et;
                    if(event.getEntity() instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) event.getEntity();
                        if(!living.hasMetadata("cantburn")) { z.setTarget(living); }
                    }
                }
            }
        }
    }

    //Event Listener saat Entity cari Target
    public void handle_entintytargeting(EntityTargetLivingEntityEvent event){
        if (event.getTarget() instanceof Player) {
            if(event.getEntity().hasMetadata(event.getTarget().getUniqueId().toString())){
                event.setCancelled(true);
                return;
            }else{
                Player p = (((Player) event.getTarget()).getPlayer());
                for (Entity et : util.getentitas(p,20)) {
                    if(et.hasMetadata(p.getUniqueId().toString())){
                        Zombie z = (Zombie) et;
                        if(event.getEntity() instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity) event.getEntity();
                            if(!living.hasMetadata("cantburn")) {
                                if(z.getTarget()!=null) {
                                    if (z.getTarget().isDead()) {
                                        z.setTarget(living);
                                        break;
                                    }
                                }else{
                                    z.setTarget(living);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.getTarget() instanceof Entity) {
            if (event.getEntityType() == EntityType.ZOMBIE && event.getTarget().getType() == EntityType.ZOMBIE) {
                if (event.getEntity().hasMetadata("cantburn")){
                    if(event.getTarget().hasMetadata("cantburn")){
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
    //Event listener untuk zombie yg kebakar pas siang hari
    public void handle_entintydibakar(EntityCombustEvent event){
        if(event.getEntityType()==EntityType.ZOMBIE){
            if(event.getEntity().hasMetadata("cantburn")) event.setCancelled(true);
        }
    }
    public MagicData search_md(String cari){
        for(MagicData md : magicdata){
            if(md.getCmd().equalsIgnoreCase(cari) || md.getName().equalsIgnoreCase(cari)) return md;
        }
        return null;
    }
}