package com.sc4r.SpecterTnT.Manager;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import com.snockmc.tnt.*;

import java.util.*;

public class TnTItem
{
    private ItemStack tntItem;
    private SnockTnT plugin;
    
    public TnTItem(final SnockTnT plugin) {
        this.plugin = plugin;
        this.setItem();
    }
    
    public void setItem() {
        final ItemStack tnt = new ItemStack(Material.TNT);
        final ItemMeta tntMeta = tnt.getItemMeta();
        tntMeta.setDisplayName(this.plugin.getConfig().getString("TnT.name").replace("&", "�"));
        final List<String> lore = new ArrayList<String>();
        for (final String string : this.plugin.getConfig().getStringList("TnT.lore")) {
            lore.add(string.replace("&", "�"));
        }
        tntMeta.setLore(lore);
        if (this.plugin.getConfig().getBoolean("TnT.glow")) {
            tntMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            tntMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        }
        tnt.setItemMeta(tntMeta);
        this.tntItem = tnt;
    }
    
    public ItemStack getTnt() {
        return this.tntItem;
    }
}
