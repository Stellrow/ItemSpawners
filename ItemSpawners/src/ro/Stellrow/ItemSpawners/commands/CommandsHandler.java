package ro.Stellrow.ItemSpawners.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.Stellrow.ItemSpawners.ItemSpawners;
import ro.Stellrow.ItemSpawners.utils.ChatUtils;

public class CommandsHandler implements CommandExecutor {
    private final ItemSpawners pl;

    public CommandsHandler(ItemSpawners pl) {
        this.pl = pl;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String sa, String[] args) {
        if(args.length==4&&args[0].equalsIgnoreCase("give")){
            if(sender.hasPermission("itemspawners.give")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target==null){
                    sender.sendMessage(ChatUtils.asColor("&cThe player is not online!"));
                    return true;
                }
                try{
                    Material material = Material.valueOf(args[2].toUpperCase());
                    Integer amount = Integer.parseInt(args[3]);
                    target.getInventory().addItem(pl.getItemBuilder().createSpawner(args[2],amount,target.getName(),1,1,0));
                    return true;
                }catch (IllegalArgumentException exception){
                    sender.sendMessage(ChatUtils.asColor("&cNot a number or a valid minecraft item!"));
                    return true;
                }

            }

        }
        return true;
    }
}
