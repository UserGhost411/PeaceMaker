package xyz.userghost.peacemaker.skill.Magic;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class MagicData {
    String id,cmd,name,description;
    int cast,cooldown,minlevel,xp_min,xp_max;
    ItemStack price;
    public MagicData(String id,String cmd,String name,String description,int cast,int cooldown,int minlevel,ItemStack price,int xp_min,int xp_max){
        this.id = id;
        this.cmd = cmd;
        this.name = name;
        this.description = description;
        this.cast = cast;
        this.cooldown = cooldown;
        this.minlevel = minlevel;
        this.price = price;
        this.xp_min = xp_min;
        this.xp_max = xp_max;
        //datafullmagic.add("񘄵񖶅;Heal;Heal;\n1st Tier\nA spell that add low health\nto caster.\n\nCast time : 2 Second\nCooldown : 10 Second\nMin level : 10;2;10;10;4500;10;20");
    }
    public String getId() {
        String hasil = "";
        for(char x : id.toCharArray()) hasil = hasil + "�" + x;
        return hasil;
    }
    public String getId(Boolean raw) {
        if(raw){
            return id;
        }else{
            String hasil = "";
            for(char x : id.toCharArray()) hasil = hasil + "�" + x;
            return hasil;
        }
    }
    public String getCmd() {
        return cmd;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getDescription(Boolean chatcolor) {
        if(chatcolor){
            return  ChatColor.translateAlternateColorCodes('&',description);
        }
        return description;
    }

    public int getCast() {
        return cast;
    }
    public int getCooldown() {
        return cooldown;
    }
    public int getMinlevel() {
        return minlevel;
    }
    public ItemStack getPrice() {
        return price;
    }
    public int getXp_min() {
        return xp_min;
    }
    public int getXp_max() {
        return xp_max;
    }
}
