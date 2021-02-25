package xyz.userghost.peacemaker.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Util {
    Plugin my;
    public Util(Plugin x) {
        my = x;
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public int subversion(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        version = version.replace("1_","X").replace("_R","Y");
        int subVersion = Integer.parseInt(version.substring(version.indexOf("X")+1,version.indexOf("Y")));
        return subVersion;
    }
    public String ServerVersion(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        version = version.replace("v","X").replace("_R","Y").replace("_",".");
        return version.substring(version.indexOf("X")+1,version.indexOf("Y"))+".x";
    }
    public void consolelog(String strx){
        ConsoleCommandSender console = my.getServer().getConsoleSender();
        console.sendMessage("[§a"+my.getName()+"§r] §f"+strx);
    }
    public void copy(InputStream in, File file, Boolean Debuger) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){ out.write(buf,0,len); }
            out.close();
            in.close();
        } catch (Exception e) {
            if(Debuger)e.printStackTrace();
        }
    }
    public List<Entity> getEntityInSight(Player p, int distance){
        ArrayList<Block> Blocks = (ArrayList<Block>) p.getPlayer().getLineOfSight((Set) null, 20);
        int sd=0;
        Block last = null;
        for (Block block : Blocks) {
            last = block;
        }
        Location location = last.getLocation();
        List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, distance, distance, distance);
        return nearbyEntites;
    }
    public static int changePlayerExp(Player player, int exp){
        int currentExp = getPlayerExp(player);
        player.setExp(0);
        player.setLevel(0);
        int newExp = currentExp + exp;
        player.giveExp(newExp);
        return newExp;
    }
    public static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }
    public int rand(int min,int max){
        int i = (int)(Math.random() * (max - min + 1) + min);
        return i;
    }
    public static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    public static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();
        exp += getExpAtLevel(level);
        exp += Math.round(getExpToLevelUp(level) * player.getExp());
        return exp;
    }
    public List<Entity> getentitas(Player player, int distance) {
        Location location = player.getLocation();
        List<Entity> nearbyEntites = (List<Entity>) location.getWorld().getNearbyEntities(location, distance, distance, distance);
        return nearbyEntites;
    }
    public List<Entity> getnearbyentitys(Location location, int distance) {
        return  (List<Entity>) location.getWorld().getNearbyEntities(location, distance, distance, distance);
    }
    public List<Player> getPlayersWithin(Player player, int distance) {
        List<Player> res = new ArrayList<Player>();
        int d2 = distance * distance;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) res.add(p);
        }
        return res;
    }
    public static String getCardinalDirection(Player player) {
        double rotation = player.getLocation().getYaw();
        if (rotation > 315 && rotation < 45) {
            return "N";
        } else if (rotation > 45 && rotation < 135) {
            return "E";
        } else if (rotation > 135 && rotation < 225) {
            return "S";
        } else if (rotation > 225 && rotation < 315) {
            return "W";
        } else {
            return "N";
        }
    }
    public void sendActionbar(Player player, String msg) {
        try {
            if(subversion()==16){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            }else{
                Constructor<?> constructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), getNMSClass("ChatMessageType"));
                Object icbc = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + msg + "\"}");
                Object packet = constructor.newInstance(icbc, getNMSClass("ChatMessageType").getEnumConstants()[2]);
                Object entityPlayer= player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
