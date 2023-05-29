/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.nehorowo.whitelist.WhitelistPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static String fixColor(String string) {
        Pattern pattern = Pattern.compile("&(#[a-fA-F0-9]{6})");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start() + 1, matcher.end());
            string = string.replace("&" + color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }


    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(fixColor(title), fixColor(subtitle), 10, 35, 10);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(fixColor(text));
    }

    public static void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(fixColor(WhitelistPlugin.getInstance().getConfig().getString("messages.correct-usage").replace("[USAGE]", usage)));
    }

    public static void sendLogger(String text, WhitelistPlugin plugin) {
        plugin.getServer().getLogger().info(fixColor("&8[&6" + plugin.getName() + "&8] &e" + text));
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(fixColor(message)));
    }
}
