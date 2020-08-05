package ro.Stellrow.ItemSpawners.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class ItemSpawnerBreak implements Listener {
    private final ItemSpawners pl;

    public ItemSpawnerBreak(ItemSpawners pl) {
        this.pl = pl;
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getType()!= Material.SPAWNER){
            return;
        }
        if (event.getPlayer().getGameMode()== GameMode.CREATIVE){
            return;
        }
        if(event.getPlayer().getInventory().getItemInMainHand()==null){
            return;
        }
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().toString().endsWith("_PICKAXE")){
            return;
        }
        if(!event.getPlayer().getInventory().getItemInMainHand().hasItemMeta()&&event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){
            return;
        }
        CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlock().getState();
        if(!creatureSpawner.getPersistentDataContainer().has(NameSK.spawnerKey, PersistentDataType.STRING)){
            return;
        }
        PersistentDataContainer pdc = creatureSpawner.getPersistentDataContainer();
        String type = pdc.get(NameSK.spawnerType,PersistentDataType.STRING);
        String owner = pdc.get(NameSK.spawnerOwner,PersistentDataType.STRING);
        Integer quantity = pdc.get(NameSK.spawnerQuantity,PersistentDataType.INTEGER);
        Integer speed = pdc.get(NameSK.spawnerSpeed,PersistentDataType.INTEGER);
        Integer hologramStatus = pdc.get(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER);
        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(),pl.getItemBuilder().createSpawner(type,1,owner,quantity,speed,hologramStatus));
        pl.getSpawnerLogicManager().removeSpawnerLogic(event.getBlock());
        return;
    }
}
