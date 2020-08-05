package ro.Stellrow.ItemSpawners.events;

import ro.Stellrow.ItemSpawners.ItemSpawners;

public class EventsManager {
    private final ItemSpawners pl;

    public EventsManager(ItemSpawners pl) {
        this.pl = pl;
    }

    public void registerEvents(){
        pl.getServer().getPluginManager().registerEvents(new ItemSpawnerSpawnEvent(pl),pl);
        pl.getServer().getPluginManager().registerEvents(new ItemSpawnerPlace(pl),pl);
        pl.getServer().getPluginManager().registerEvents(new ItemSpawnerBreak(pl),pl);
        pl.getServer().getPluginManager().registerEvents(new ItemSpawnerInteract(pl),pl);
        //Unneeded? Spawners seem to be immune to most pistons or actions
        //pl.getServer().getPluginManager().registerEvents(new SpawnerWorldInfluenceChecks(),pl);

    }
}
