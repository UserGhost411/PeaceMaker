package xyz.userghost.peacemaker.skill.Magic;

import org.bukkit.inventory.ItemStack;

public class hotbar_data {
    ItemStack is_1,is_2,is_3,is_4,is_5,is_6,is_7,is_8;
    public hotbar_data(ItemStack is1, ItemStack is2, ItemStack is3, ItemStack is4, ItemStack is5, ItemStack is6, ItemStack is7, ItemStack is8){
        is_1 = is1;
        is_2 = is2;
        is_3 = is3;
        is_4 = is4;
        is_5 = is5;
        is_6 = is6;
        is_7 = is7;
        is_8 = is8;
    }
    public ItemStack getIs_1() {
        return is_1;
    }
    public ItemStack getIs_2() {
        return is_2;
    }
    public ItemStack getIs_3() {
        return is_3;
    }
    public ItemStack getIs_4() {
        return is_4;
    }
    public ItemStack getIs_5() {
        return is_5;
    }
    public ItemStack getIs_6() {
        return is_6;
    }
    public ItemStack getIs_7() {
        return is_7;
    }
    public ItemStack getIs_8() {
        return is_8;
    }
}
