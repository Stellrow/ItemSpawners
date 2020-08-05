package ro.Stellrow.ItemSpawners.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class ItemSpawnerInteract implements Listener {
    private final ItemSpawners pl;

    public ItemSpawnerInteract(ItemSpawners pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onRClick(PlayerInteractEvent event){
        if(event.getAction()!= Action.RIGHT_CLICK_BLOCK){
            return;
        }
        Block b = event.getClickedBlock();
        if(b.getType()!= Material.SPAWNER){
            return;
        }
        CreatureSpawner creatureSpawner = (CreatureSpawner) b.getState();
        if(!creatureSpawner.getPersistentDataContainer().has(NameSK.spawnerKey, PersistentDataType.STRING)){
            return;
        }

        pl.getGuiHandler().openGUI(event.getPlayer(),creatureSpawner,pl.getSpawnerLogicManager().getSpawnerLogic(b));
        return;

    }

}
