package ro.Stellrow.ItemSpawners.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class SpawnerWorldInfluenceChecks implements Listener {

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent event){
        for(Block b : event.getBlocks()){
            if(b.getType()== Material.SPAWNER){
                CreatureSpawner creatureSpawner = (CreatureSpawner) b.getState();
                if(creatureSpawner.getPersistentDataContainer().has(NameSK.spawnerKey, PersistentDataType.STRING)){
                    event.setCancelled(true);
                }
            }
        }
    }
}
