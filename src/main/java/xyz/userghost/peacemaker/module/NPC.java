package xyz.userghost.peacemaker.module;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import xyz.userghost.peacemaker.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NPC  implements Listener {
    Plugin my;
    Util util;
    File configFile;
    FileConfiguration config;
    ArrayList<LivingEntity> dbentity = new ArrayList<LivingEntity>();

    public NPC(Plugin x) {
        my = x;
        util = new Util(x);
        Bukkit.getServer().getPluginManager().registerEvents(this, my);
        util.consolelog("NPC Class Loaded");
    }
    @EventHandler
    private void onjoin(PlayerJoinEvent event) {
    }
    @EventHandler
    private void onmanage(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            Inventory inventory = event.getInventory();
            InventoryView invenview = event.getView();
            OfflinePlayer player1 = player;
            if (invenview.getTitle().equals("§9NPC Management")) {
                if(clicked!=null) {
                    if (clicked.getType() != Material.AIR && clicked.getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        if (event.isLeftClick()) {

                            List<String> ls = config.getStringList("npc.list");
                            String dx[] = ls.get(event.getSlot()).split(";");
                            Location hehe = player.getLocation();
                            String cmdnya = dx[4];
                            LivingEntity ee = dbentity.get(event.getSlot());
                            ls.remove(event.getSlot());
                            ls.add(hehe.getWorld().getName().toString() + ";" + calcloc(Double.valueOf(hehe.getBlock().getX()), 0.5) + ";" + Double.valueOf(hehe.getBlock().getY()) + ";" + calcloc(Double.valueOf(hehe.getBlock().getZ()), 0.5) + ";" + cmdnya);

                            config.set("npc.list", ls);
                            config.save(configFile);
                            ee.teleport(hehe);
                            player.closeInventory();
                            player.sendMessage("move:" + event.getSlot());
                            return;
                        } else if (event.isRightClick()) {
                            List<String> ls = config.getStringList("npc.list");
                            ls.remove(event.getSlot());
                            LivingEntity ee = dbentity.get(event.getSlot());
                            Bukkit.getScheduler().cancelTask(Integer.parseInt(ee.getMetadata("lookatbro").get(0).value().toString()));
                            ee.remove();
                            dbentity.remove(event.getSlot());
                            config.set("npc.list", ls);
                            config.save(configFile);
                            player.closeInventory();
                            player.sendMessage("delete:" + event.getSlot());
                            return;
                        }
                    }
                }
                event.setCancelled(true);
            }

        } catch (Exception e) {
            util.consolelog(e.getMessage());
        }
    }
    @EventHandler
    private void onnpcclick(PlayerInteractEntityEvent event) {
        try {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (event.getRightClicked().getType() == EntityType.VILLAGER) {
                    if (event.getRightClicked().hasMetadata("peacemakernpc")) {
                        event.getPlayer().performCommand(event.getRightClicked().getMetadata("cmdnpc").get(0).value().toString());
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_TRADE, 3.0F, 1);
                        event.setCancelled(true);
                    }
                }
            }
        } catch (Exception e) {
            util.consolelog("PlayerInteractEntityEvent\n" + e.getMessage());
        }
    }

    public void call_enabled() {
        configFile = new File(my.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            util.copy(my.getResource("config.yml"), configFile, false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
            for (String le : config.getStringList("npc.list")) {
                String dx[] = le.split(";");
                Location hehe = new Location(Bukkit.getWorld(dx[0]), Double.valueOf(dx[1]), Double.valueOf(dx[2]), Double.valueOf(dx[3]));
                String cmdnya = dx[4];
                addnpc(hehe, cmdnya, true,EntityType.VILLAGER,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(my, new Runnable() {
            public void run() {
                for (LivingEntity le : dbentity) {
                    le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (6000 * 20), 255, false, false));
                }
            }
        }, 20 * 1800, 20 * 5);
    }
    public void npcmanage(Player p){
        if(!p.isOp()) {
            p.sendMessage("[PeaceMaker] You Dont Have Any Access to this commands");
            return;
        }
        int opo = 0;
        Inventory inv = Bukkit.createInventory(p,9*5,"§9NPC Management");
        for (LivingEntity le : dbentity) {
            ItemStack is = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("NPC "+opo);
            List<String> ha = new ArrayList<String>();
            ha.add(le.getWorld().getName()+" "+le.getLocation().getBlockX()+"/"+le.getLocation().getBlockY()+"/"+le.getLocation().getBlockZ());
            ha.add("Command:");
            ha.add(le.getMetadata("cmdnpc").get(0).value().toString());
            ha.add("§1");
            ha.add("§aRight Click to Delete NPC | Left Click to Move");
            im.setLore(ha);
            is.setItemMeta(im);
            inv.setItem(opo,is);
            opo++;
        }
        while(opo<9*5){
            inv.setItem(opo,new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            opo++;
        }
        p.openInventory(inv);
    }
    public void call_disabled() {
        for (LivingEntity le : dbentity) {
            Bukkit.getScheduler().cancelTask(Integer.parseInt(le.getMetadata("lookatbro").get(0).value().toString()));
            le.remove();
        }
    }
    private Double calcloc(Double a, Double b) {
        return (a + b);
    }
    public void addnpc(Location loc, String cmdx, Boolean bydb,EntityType lenya,Boolean checking) {
        try {
            if(checking){
                for(Entity checker : loc.getWorld().getNearbyEntities(loc,1,1,1)) {
                    if (checker instanceof LivingEntity) checker.remove();
                }
            }
            LivingEntity lel;
            if(lenya==EntityType.WITCH){
                Witch witch = loc.getWorld().spawn(loc, Witch.class);
                lel = witch;
            }else{
                Villager vil = loc.getWorld().spawn(loc, Villager.class);
                vil.setAdult();
                if(cmdx.equalsIgnoreCase("mershop currency")){
                    vil.setProfession(Villager.Profession.CARTOGRAPHER);
                }else if(cmdx.equalsIgnoreCase("mershop blacksmith")){
                    vil.setProfession(Villager.Profession.WEAPONSMITH);
                }else if(cmdx.equalsIgnoreCase("mershop food")){
                    vil.setProfession(Villager.Profession.BUTCHER);
                }else if(cmdx.equalsIgnoreCase("mershop loot")){
                    vil.setProfession(Villager.Profession.FLETCHER);
                }else if(cmdx.equalsIgnoreCase("spell shop home")) {
                    vil.setProfession(Villager.Profession.CLERIC);
                }
                lel = vil;
            }
            final LivingEntity le = lel;
            int lookatbro = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(my, new Runnable() {
                public void run() {
                    if(le.getLocation().getChunk().isLoaded()){
                        if (le != null) {
                            for (Entity et : le.getNearbyEntities(10, 10, 10)) {
                                if (et instanceof Player) {
                                    Vector dirBetweenLocations = et.getLocation().toVector().subtract(le.getLocation().toVector());
                                    Location loc = le.getLocation();
                                    loc.setDirection(dirBetweenLocations);
                                    le.teleport(loc);
                                }
                            }
                        }
                    }
                }
            }, 10, 10);
            le.setAI(false);
            le.setInvulnerable(true);
            le.setCanPickupItems(false);
            le.setRemoveWhenFarAway(false);
            le.setMetadata("peacemakernpc", new FixedMetadataValue(my, "test"));
            le.setMetadata("customnpc", new FixedMetadataValue(my, "PeaceMaker"));
            le.setMetadata("lookatbro", new FixedMetadataValue(my, lookatbro));
            le.setMetadata("cmdnpc", new FixedMetadataValue(my, cmdx));
            dbentity.add(le);
            if (!bydb) {
                List<String> ls = config.getStringList("npc.list");
                ls.add(loc.getWorld().getName().toString() + ";" + calcloc(Double.valueOf(loc.getBlock().getX()), 0.5) + ";" + Double.valueOf(loc.getBlock().getY()) + ";" + calcloc(Double.valueOf(loc.getBlock().getZ()), 0.5) + ";" + cmdx);
                config.set("npc.list", ls);
                config.save(configFile);
            }
        } catch (Exception er) {
           // util.consolelog("addnpc\n" + er.getMessage());
        }
    }
    public static Location faceLocation(Entity entity, Location to) {
        if (entity.getWorld() != to.getWorld()) { return null; }
        Location fromLocation = entity.getLocation();
        double xDiff = to.getX() - fromLocation.getX();
        double yDiff = to.getY() - fromLocation.getY();
        double zDiff = to.getZ() - fromLocation.getZ();
        double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);
        double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
        double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
        if (zDiff < 0.0D) {
            yaw += Math.abs(180.0D - yaw) * 2.0D;
        }
        Location loc = entity.getLocation();
        loc.setYaw((float) (yaw - 90.0F));
        loc.setPitch((float) (pitch - 90.0F));
        return loc;
    }
}