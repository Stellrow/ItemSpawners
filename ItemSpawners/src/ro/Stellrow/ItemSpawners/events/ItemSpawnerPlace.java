package ro.Stellrow.ItemSpawners.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class ItemSpawnerPlace implements Listener {
    private final ItemSpawners pl;

    public ItemSpawnerPlace(ItemSpawners pl) {
        this.pl = pl;
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        if(item==null){
            return;
        }
        if(!item.hasItemMeta()){
            return;
        }
        if(item.getItemMeta().getPersistentDataContainer().has(NameSK.spawnerKey, PersistentDataType.STRING)){
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

            String type = pdc.get(NameSK.spawnerType,PersistentDataType.STRING);
            String owner = pdc.get(NameSK.spawnerOwner,PersistentDataType.STRING);
            Integer quantity = pdc.get(NameSK.spawnerQuantity,PersistentDataType.INTEGER);
            Integer speed = pdc.get(NameSK.spawnerSpeed,PersistentDataType.INTEGER);
            Integer hologramStatus = pdc.get(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER);
            CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockPlaced().getState();
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerKey,PersistentDataType.STRING,"ItemSpawner");
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerType,PersistentDataType.STRING,type);
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerOwner,PersistentDataType.STRING,owner);
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerQuantity,PersistentDataType.INTEGER,quantity);
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerSpeed,PersistentDataType.INTEGER,speed);
            creatureSpawner.getPersistentDataContainer().set(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER,hologramStatus);
            creatureSpawner.update();
            changeSpawner(event.getBlockPlaced(),type);

            return;
        }

    }
    private void changeSpawner(Block b, String type){
        new BukkitRunnable() {
            @Override
            public void run() {
                pl.nbtModule.setSpawner(b,type.toLowerCase());
            }
        }.runTaskLater(pl,1);
    }
}
