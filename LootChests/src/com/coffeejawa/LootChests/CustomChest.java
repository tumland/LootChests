package com.coffeejawa.LootChests;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class CustomChest {

    private Block chestBlock;
    private float timeout;
    
    CustomChest(Block b, float timeout){
        chestBlock = b;
        this.timeout = System.currentTimeMillis() + timeout * 1000;
    }
    
    public Boolean hasTimeoutExpired(){
        if( System.currentTimeMillis() > this.timeout ){
            // chest has timed out
            return true;
        }
        return false;
    }
    
    public void tearDown(){
        this.chestBlock.setType(Material.AIR);
    }
}
