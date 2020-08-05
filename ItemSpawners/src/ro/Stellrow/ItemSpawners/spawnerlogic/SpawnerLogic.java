package ro.Stellrow.ItemSpawners.spawnerlogic;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;

public class SpawnerLogic {
    private final ItemSpawners main;
    private CreatureSpawner creatureSpawner;
    private String type;
    private Integer quantity;
    private Integer speed;
    private Integer hologramStatus;
    private Integer currentTime;
    private Integer timer = 0;
    private Hologram holo;
    private TextLine tl;
    private Integer speedMultiplier;
    private boolean toCancel = false;

    public SpawnerLogic(CreatureSpawner spawnerInvolved, ItemSpawners main){
        this.creatureSpawner=spawnerInvolved;
        this.main=main;
        currentTime = main.getConfig().getInt("General.spawnerOptions.starting-time");
        speedMultiplier= main.getConfig().getInt("General.spawnerOptions.speed-multiplier");
    }

    public void destroy(){
        removeHologram();
        toCancel = true;
    }
    public void storeData(){
        PersistentDataContainer pdc = creatureSpawner.getPersistentDataContainer();
        type = pdc.get(NameSK.spawnerType, PersistentDataType.STRING);
        quantity = pdc.get(NameSK.spawnerQuantity,PersistentDataType.INTEGER);
        speed = pdc.get(NameSK.spawnerSpeed,PersistentDataType.INTEGER);
        hologramStatus = pdc.get(NameSK.spawnerHologramStatus,PersistentDataType.INTEGER);
    }
    public void startLogic(){
        refreshTimer(speed);
        startCounting();

        if(hologramStatus.equals(1)){
            tryCreateHologram();
        }
    }
    public void tryCreateHologram(){
        if(!main.useHolographicDisplays){
            return;
        }
                    holo = HologramsAPI.createHologram(main, creatureSpawner.getLocation().add(0.5, 1.5, 0.5));
                    tl = holo.appendTextLine(ChatColor.AQUA+type.toUpperCase()+ChatColor.GRAY+" | "+ChatColor.GOLD+ returnHologramTime());
                    updateHologram();
    }
    public void removeHologram() {
        holo.delete();
    }
    private void updateHologram() {
        new BukkitRunnable() {

            @Override
            public void run() {
                if(holo==null){
                    this.cancel();
                }
                if(toCancel){
                    this.cancel();
                    return;
                }
                tl.setText(ChatColor.AQUA+type.toUpperCase()+ChatColor.GRAY+" | "+ChatColor.GOLD+ returnHologramTime());
            }

        }.runTaskTimer(main, 0, 20);
    }
    private void startCounting(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(toCancel){
                    this.cancel();
                    return;
                }
                timer++;
                if(timer>=currentTime){
                    timer=0;
                    //Spawn item
                    drop(creatureSpawner.getLocation(),new ItemStack(Material.valueOf(type.toUpperCase()),quantity));
                    return;
                }

            }
        }.runTaskTimerAsynchronously(main,0,20);
    }
    private void drop(Location location,ItemStack toDrop){
        new BukkitRunnable(){

            @Override
            public void run() {
            location.getWorld().dropItemNaturally(location,toDrop);
            }
        }.runTask(main);
    }


    //Timing
    private int[] getMinutes(Integer time) {
        int remTime = time;
        int minutes = 0;
        while(remTime>=60) {
            remTime-=60;
            minutes++;
        }
        return new int[] {minutes,remTime};
    }

    private String minutes = " minutes ";
    private String and = "and ";
    private String seconds = " seconds";

    private String returnHologramTime() {
        int[] ttime = getMinutes((currentTime-timer));
        String toSet = null;
        if(ttime[0]>=1) {
            if(ttime[1]>=1) {
                toSet=ttime[0]+minutes+and+ttime[1]+seconds;
            }else {
                toSet=ttime[0]+minutes;
            }
        }else {
            toSet=ttime[1]+seconds;
        }
        return toSet;
    }
    private void refreshTimer(Integer speedLevel){
        Integer percentage = speedLevel*speedMultiplier;
        currentTime = currentTime - (int)((currentTime*(percentage/100.0f)));
    }

    }

