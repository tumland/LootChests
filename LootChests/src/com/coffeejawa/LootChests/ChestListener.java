package com.coffeejawa.LootChests;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {
    private LootChestsMain plugin;
    
    private final int chestID = Material.CHEST.getId();
    
    ChestListener(LootChestsMain plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        // if its not a chest, we don't care
        if((block == null) || (block.getTypeId() != chestID)){
            return;
        }
        
        // if its not in our map, we don't care
        if(!plugin.getChestPlayerMap().containsKey(block)){
            return;
        }
        
        Player chestOwner = plugin.getChestPlayerMap().get(block);
        if(!chestOwner.equals(event.getPlayer())){
            // block access
            event.setCancelled(true);
        }
        
        // remove chest if empty
        // TODO : this needs to be done after Player removes an item, not when first interacting
        Chest chest = (Chest) block;
        if(chest.getBlockInventory().getSize() == 0){
            chest.setType(Material.AIR);
            event.setCancelled(true);
        }
        
    }
    
    
    
}
