package ro.Stellrow.ItemSpawners.itembuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;
import ro.Stellrow.ItemSpawners.utils.ChatUtils;

public class ItemBuilder {
    private final ItemSpawners pl;

    public ItemBuilder(ItemSpawners pl) {
        this.pl = pl;
    }
    public ItemStack createSpawner(String type, Integer amount, String owner,Integer quantity,Integer speed,Integer hologramStatus){
        ItemStack toRet = new ItemStack(Material.SPAWNER,amount);
        ItemMeta im = toRet.getItemMeta();
        im.setDisplayName(ChatUtils.asColor(pl.getConfig().getString("SpawnerItemConfig.name").replaceAll("%spawnerType",type.toUpperCase())));
        im.setLore(ChatUtils.coloredLore(pl.getConfig().getStringList("SpawnerItemConfig.lore")));
        im.getPersistentDataContainer().set(NameSK.spawnerKey, PersistentDataType.STRING,"ItemSpawner");
        im.getPersistentDataContainer().set(NameSK.spawnerOwner, PersistentDataType.STRING,owner);
        im.getPersistentDataContainer().set(NameSK.spawnerQuantity,PersistentDataType.INTEGER,quantity);
        im.getPersistentDataContainer().set(NameSK.spawnerSpeed,PersistentDataType.INTEGER,speed);
        im.getPersistentDataContainer().set(NameSK.spawnerType,PersistentDataType.STRING,type.toLowerCase());
        im.getPersistentDataContainer().set(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER,hologramStatus);
        toRet.setItemMeta(im);
        return toRet;
    }

}
