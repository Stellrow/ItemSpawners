package ro.Stellrow.ItemSpawners.spawnerlogic;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.ItemSpawners.ItemSpawners;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnerLogicManager {
    private final ItemSpawners pl;

    public SpawnerLogicManager(ItemSpawners pl) {
        this.pl = pl;
    }
    private ConcurrentHashMap<Block,SpawnerLogic> spawnerLogics = new ConcurrentHashMap<>();

    public void addSpawnerLogic(Block b,@Nullable CreatureSpawner spawnerInvolved){
        if(hasSpawnerLogic(b)){
            return;
        }else {
            SpawnerLogic spawnerLogic = new SpawnerLogic(spawnerInvolved, pl);
            spawnerLogic.storeData();
            spawnerLogic.startLogic();
            spawnerLogics.put(b, spawnerLogic);
        }
    }
    public void removeSpawnerLogic(Block b){
        if(hasSpawnerLogic(b)) {
            spawnerLogics.get(b).destroy();
            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnerLogics.remove(b);
                }
            }.runTaskLater(pl,25);

        }
    }
    public boolean hasSpawnerLogic(Block b){
        if(spawnerLogics.containsKey(b)){
            return true;
        }
        return false;
    }
    public SpawnerLogic getSpawnerLogic(Block b){
        if(hasSpawnerLogic(b)){
            return spawnerLogics.get(b);
        }
        return null;
    }
}
