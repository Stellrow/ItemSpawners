package ro.Stellrow.ItemSpawners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ro.Stellrow.ItemSpawners.commands.CommandsHandler;
import ro.Stellrow.ItemSpawners.events.EventsManager;
import ro.Stellrow.ItemSpawners.gui.GuiHandler;
import ro.Stellrow.ItemSpawners.itembuilder.ItemBuilder;
import ro.Stellrow.ItemSpawners.namespacedkeys.NameSK;
import ro.Stellrow.ItemSpawners.nms.NBTModule;
import ro.Stellrow.ItemSpawners.nms.NBTModule_1_14_R1;
import ro.Stellrow.ItemSpawners.nms.NBTModule_1_15_R1;
import ro.Stellrow.ItemSpawners.nms.NBTModule_1_16_R1;
import ro.Stellrow.ItemSpawners.spawnerlogic.SpawnerLogicManager;


public class ItemSpawners extends JavaPlugin {
    public NBTModule nbtModule;
    private EventsManager eventsManager = new EventsManager(this);
    private ItemBuilder itemBuilder = new ItemBuilder(this);
    private SpawnerLogicManager spawnerLogicManager = new SpawnerLogicManager(this);
    private GuiHandler guiHandler = new GuiHandler(this);
    private NameSK nameSK = new NameSK(this);


    //APIs
    public boolean useHolographicDisplays = false;
    public boolean hasEconomy = false;
    public static Economy economy = null;


    public void onEnable(){
        implementCorrectNBT();
        loadConfig();
        getCommand("itemspawners").setExecutor(new CommandsHandler(this));
        eventsManager.registerEvents();
        guiHandler.registerEvents();
        setupDependencies();
    }
    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }




    private void implementCorrectNBT(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace('.', ',').split(",")[3];
        System.out.println("[ItemSpawners] Server Version: " + version);
        switch (version) {
            case "v1_14_R1":
                nbtModule = new NBTModule_1_14_R1();
                break;
            case "v1_15_R1":
                nbtModule = new NBTModule_1_15_R1();
                break;
            case "v1_16_R1":
                nbtModule = new NBTModule_1_16_R1();
                break;
            default: {
                getLogger().info("Unsupported version of Minecraft");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }




    private void setupDependencies(){
        hookHolographicDisplays();
        findVault();
        loadEconomy();
    }



    public ItemBuilder getItemBuilder(){
        return itemBuilder;
    }
    public SpawnerLogicManager getSpawnerLogicManager(){
        return spawnerLogicManager;
    }
    public GuiHandler getGuiHandler(){
        return guiHandler;
    }




    //HolographicDisplays hook
    private void hookHolographicDisplays() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            @Override
            public void run(){
                if(!getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
                    getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ ChatColor.RED+" HolographicDisplays was not found,please install it before using this plugin!");
                    return;
                }
                getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ChatColor.GREEN+" HolographicDisplays was found! Holograms features are now enabled!");
                useHolographicDisplays = true;
                return;
            }
        });
    }



    //Economy
    private void loadEconomy() {
        new BukkitRunnable() {

            @Override
            public void run() {
                checkEconomy();
            }

        }.runTaskLaterAsynchronously(this, 40);
    }
    public void findVault() {
        if(getServer().getPluginManager().getPlugin("Vault")!=null&&getServer().getPluginManager().isPluginEnabled("Vault")) {
            setupEconomy();
            hasEconomy=true;
            getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ChatColor.GREEN+" Found Vault,hooking into economy");
            return;
        }
        getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ChatColor.RED+" Vault wasnt found! Level up using money feature is disabled!");
        return;
    }
    private void checkEconomy() {
        if(economy==null) {
            hasEconomy = false;
            getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ChatColor.RED+" No economy provider was found,money based features are disabled");
            return;
        }
        getServer().getConsoleSender().sendMessage("[ItemSpawners]"+ChatColor.GREEN+" Found economy provider,money based features are enabled!");
        return;

    }
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }


}

