package ro.Stellrow.ItemSpawners.gui;

import com.mysql.fabric.xmlrpc.base.Array;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;
import ro.Stellrow.ItemSpawners.spawnerlogic.SpawnerLogic;
import ro.Stellrow.ItemSpawners.utils.ChatUtils;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class GuiHandler implements Listener {
    private final ItemSpawners pl;
    private Integer maxSpeedLevel;
    private Integer maxQuantityLevel;
    private Integer base_upgrade_cost;
    


    public GuiHandler(ItemSpawners pl) {
        this.pl = pl;
        maxSpeedLevel = pl.getConfig().getInt("General.spawnerUpgrade.speed-level-limit");
        maxQuantityLevel = pl.getConfig().getInt("General.spawnerUpgrade.quantity-level-limit");
        upgradeCost = new NamespacedKey(pl,"upgradecost");
        upgradeType = new NamespacedKey(pl,"upgradetype");
        base_upgrade_cost = pl.getConfig().getInt("General.upgradeOptions.base-upgrade-cost");
    }

    public void registerEvents(){
        pl.getServer().getPluginManager().registerEvents(this,pl);
    }
    private ConcurrentHashMap<Inventory, Pair<CreatureSpawner,SpawnerLogic>> activeInventories = new ConcurrentHashMap<>();

public void openGUI(Player player, CreatureSpawner creatureSpawner, SpawnerLogic spawnerLogic){
    Inventory inv = Bukkit.createInventory(null,27, ChatUtils.asColor("&aSpawner"));
    String type = creatureSpawner.getPersistentDataContainer().get(NameSK.spawnerType,PersistentDataType.STRING);
    Integer speed = creatureSpawner.getPersistentDataContainer().get(NameSK.spawnerSpeed,PersistentDataType.INTEGER);
    Integer quantity = creatureSpawner.getPersistentDataContainer().get(NameSK.spawnerQuantity, PersistentDataType.INTEGER);
    Integer hologramStatus = creatureSpawner.getPersistentDataContainer().get(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER);
    inv.setItem(10,buildType(type));

    inv.setItem(12,buildSpeed(speed));
    if(speed<maxSpeedLevel){
        inv.setItem(21,buildUpgradeButton("Speed",speed*base_upgrade_cost));
    }

    inv.setItem(14,buildQuantity(quantity));
    if(quantity<maxQuantityLevel){
        inv.setItem(23,buildUpgradeButton("Quantity",quantity*base_upgrade_cost));
    }

    inv.setItem(16,buildHologram(hologramStatus));

    Pair<CreatureSpawner,SpawnerLogic> spawnerData = new Pair<>(creatureSpawner,spawnerLogic);
    activeInventories.put(inv,spawnerData);
    player.openInventory(inv);
}


    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(activeInventories.containsKey(event.getInventory())){
            activeInventories.remove(event.getInventory());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
    if(event.getCurrentItem()==null){
        return;
    }
        if(activeInventories.containsKey(event.getInventory())){
            event.setCancelled(true);
            Pair<CreatureSpawner,SpawnerLogic> spawnerData = activeInventories.get(event.getInventory());


            if(event.getCurrentItem().getType()==Material.ARMOR_STAND){
                Integer hologramStatus = spawnerData.getKey().getPersistentDataContainer().get(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER);
                switch (hologramStatus){
                    case 0:
                        spawnerData.getKey().getPersistentDataContainer().set(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER,1);
                        spawnerData.getKey().update();
                        spawnerData.getValue().tryCreateHologram();

                        event.getWhoClicked().closeInventory();
                        break;
                    case 1:
                        spawnerData.getKey().getPersistentDataContainer().set(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER,0);
                        spawnerData.getValue().removeHologram();
                        spawnerData.getKey().update();
                        event.getWhoClicked().closeInventory();
                        break;
                }
                return;
            }



        }
        return;

    }


    private ItemStack buildType(String type){
    ItemStack i = new ItemStack(Material.valueOf(type.toUpperCase()));
    ItemMeta im = i.getItemMeta();
    im.setDisplayName(ChatUtils.asColor("&aType: " +"&6"+type.toUpperCase()));
    i.setItemMeta(im);
    return i;
    }
    private ItemStack buildSpeed(Integer level){
    ItemStack i = new ItemStack(Material.SUGAR);
    ItemMeta im = i.getItemMeta();
    im.setDisplayName(ChatUtils.asColor("&aCurrent speed level: "+ "&6"+level));
    i.setItemMeta(im);
    return i;
    }
    private ItemStack buildQuantity(Integer level){
        ItemStack i = new ItemStack(Material.EMERALD);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatUtils.asColor("&aCurrent quantity level: "+ "&6"+level));
        i.setItemMeta(im);
        return i;
    }
    private ItemStack buildHologram(Integer status){
        String hstatus = "";

        switch (status){
            case 0:
                hstatus = "hidden";
                break;
            case 1:
                hstatus = "active";
                break;
        }

        ItemStack i = new ItemStack(Material.ARMOR_STAND);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ChatUtils.asColor("&aHologram status: "+ "&6"+ hstatus));
        i.setItemMeta(im);
        return i;
    }


    private NamespacedKey upgradeCost;
    private NamespacedKey upgradeType;
    private ItemStack buildUpgradeButton(String type,Integer cost){
    ItemStack i = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemMeta im = i.getItemMeta();
    im.setDisplayName(ChatUtils.asColor("&aUpgrade | &6"+type));
    switch (type){
        case "Speed":
            im.setLore(ChatUtils.coloredLore(Arrays.asList("&7To upgrade this spawner","&7You must pay " + "&a"+cost+"$","&7Upgrading this will speed up the spawning rate!")));
            break;
        case "Quantity":
            im.setLore(ChatUtils.coloredLore(Arrays.asList("&7To upgrade this spawner","&7You must pay " + "&a"+cost+"$","&7Upgrading this will give more items when spawning!")));
            break;
    }
    im.getPersistentDataContainer().set(upgradeCost,PersistentDataType.INTEGER,cost);
    im.getPersistentDataContainer().set(upgradeType,PersistentDataType.STRING,type.toLowerCase());
    i.setItemMeta(im);
    return i;
    }



}
