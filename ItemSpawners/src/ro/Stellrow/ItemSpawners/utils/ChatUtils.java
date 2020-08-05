package ro.Stellrow.ItemSpawners.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils {
    public static String asColor(String toTranslate){
        return ChatColor.translateAlternateColorCodes('&',toTranslate);
    }
    public static List<String> coloredLore(List<String> toTranslate){
        List<String> toRet = new ArrayList<>();
        for(String s : toTranslate){
            toRet.add(ChatColor.translateAlternateColorCodes('&',s));
        }
        return toRet;
    }
}
