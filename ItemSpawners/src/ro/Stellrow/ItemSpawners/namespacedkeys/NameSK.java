package ro.Stellrow.ItemSpawners.namespacedkeys;

import org.bukkit.NamespacedKey;
import ro.Stellrow.ItemSpawners.ItemSpawners;

public class NameSK {
    private final ItemSpawners pl;

    public NameSK(ItemSpawners pl) {
        this.pl = pl;
        createNSK(pl);
    }
    private void createNSK(ItemSpawners main){
        spawnerKey = new NamespacedKey(main,"spawnerkey");
        spawnerType = new NamespacedKey(main,"spawnertype");
        spawnerQuantity = new NamespacedKey(main,"spawnerquantity");
        spawnerSpeed = new NamespacedKey(main,"spawnerspeed");
        spawnerOwner = new NamespacedKey(main,"spawnerowner");
        spawnerHologramStatus = new NamespacedKey(main,"spawnerhologram");
    }
    public static NamespacedKey spawnerKey;
    public static NamespacedKey spawnerType;
    public static NamespacedKey spawnerQuantity;
    public static NamespacedKey spawnerSpeed;
    public static NamespacedKey spawnerOwner;
    public static NamespacedKey spawnerHologramStatus;
}
