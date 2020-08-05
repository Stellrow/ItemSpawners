package ro.Stellrow.ItemSpawners.events;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class ItemSpawnerSpawnEvent implements Listener {
    private final ItemSpawners pl;

    public ItemSpawnerSpawnEvent(ItemSpawners pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event){
        CreatureSpawner creatureSpawner = event.getSpawner();
        if(creatureSpawner.getPersistentDataContainer().has(NameSK.spawnerKey, PersistentDataType.STRING)){
            pl.getSpawnerLogicManager().addSpawnerLogic(creatureSpawner.getBlock(),creatureSpawner);
            event.setCancelled(true);
            return;
        }
    }
}
