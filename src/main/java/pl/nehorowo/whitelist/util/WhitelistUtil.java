/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist.util;

import pl.nehorowo.whitelist.WhitelistPlugin;

import java.util.ArrayList;
import java.util.List;

public class WhitelistUtil {

    public static boolean isWhitelisted(String player) { return WhitelistPlugin.getInstance().getConfig().getStringList("configuration.whitelist.list").contains(player); }

    public static void addWhitelist(String player) {
        List<String> list = new ArrayList<>(WhitelistPlugin.getInstance().getConfig().getStringList("configuration.whitelist.list"));
        list.add(player);
        WhitelistPlugin.getInstance().getConfig().set("configuration.whitelist.list", list);
        WhitelistPlugin.getInstance().saveConfig();
    }


    public static void removeWhitelist(String player) {
        List<String> list = new ArrayList<>(WhitelistPlugin.getInstance().getConfig().getStringList("configuration.whitelist.list"));
        list.remove(player);
        WhitelistPlugin.getInstance().getConfig().set("configuration.whitelist.list", list);
        WhitelistPlugin.getInstance().saveConfig();
    }

}
