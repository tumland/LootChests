package com.coffeejawa.LootChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LootChestsMain extends JavaPlugin  {
    public final Logger logger = Logger.getLogger("Minecraft");
    
    private HashMap<CustomChest,Player> ChestPlayerMap;
    
    public HashMap<CustomChest, Player> getChestPlayerMap() {
        return ChestPlayerMap;
    }

    @Override
    public void onEnable() 
    {        
        ChestPlayerMap = new HashMap<CustomChest, Player>();
        
        this.reloadConfig();
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        logger.info("[" + this.getDescription().getName() +  "] enabled.");
        
        
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            public void run() {
                for( CustomChest cc : ChestPlayerMap.keySet()){
                    if(cc.hasTimeoutExpired()){
                        cc.tearDown();
                        ChestPlayerMap.remove(cc);
                    }
                }
            }
         }, 0L, 600L);
    }
    
    @Override
    public void onDisable() 
    {
        logger.info("[" + this.getDescription().getName() +  "] disabled.");  
        this.saveConfig();
    }

    public Block CreateChest(Player player, Location location, ArrayList<ItemStack> items, float timeout)
    {
        // attempt to create a block 1 tile above
        location.setY(location.getY());
        
        Block b = location.getWorld().getBlockAt(location);
        if( b.getTypeId() != Material.AIR.getId() ){
            // not air, we can't create a block here
            return null;
        }
        
        // otherwise change block type from air to chest
        b.setType(Material.CHEST);
        // insert in our map
        ChestPlayerMap.put(new CustomChest(b,timeout), player);
        
        // insert items
        Chest chest = (Chest) b;
        for( ItemStack item : items ){
            chest.getBlockInventory().addItem(item);
        }
        
        return b;
    }
    
}


/* 
 * Requirements:
*   -CreateChest
*   -Remove spawned chests when empty
*   -Remove spawned non-empty chests when timeout expires
*   -Prevent unauthorized players from opening chests
*/

