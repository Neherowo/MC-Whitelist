/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import pl.nehorowo.whitelist.WhitelistPlugin;
import pl.nehorowo.whitelist.util.TextUtil;
import pl.nehorowo.whitelist.util.WhitelistUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class LoginEvent implements Listener {

    private final Plugin plugin;

    public LoginEvent(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        if(player.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("configuration.whitelist.bypass-permission")))) {
            TextUtil.sendActionBar(player, plugin.getConfig().getString("messages.whitelist-bypass.join-message")
                    .replace("[PERMISSION]", Objects.requireNonNull(plugin.getConfig().getString("configuration.whitelist.bypass-permission")))
            );
            return;
        }

        String name = player.getName();

        if(!WhitelistUtil.isWhitelisted(name)) {
            player.kickPlayer(TextUtil.fixColor(Objects.requireNonNull(plugin.getConfig().getString("messages.not-whitelisted.kick.reason"))
                    .replace("[TIME]", getTime())
            ));
            Bukkit.getOnlinePlayers().forEach(all -> { if(all.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("configuration.whitelist.bypass-permission")))) {
                TextUtil.sendMessage(all, plugin.getConfig().getString("messages.not-whitelisted.broadcast")
                        .replace("[PERMISSION]", plugin.getConfig().getString("configuration.whitelist.bypass-permission")
                        .replace("[TIME]", getTime())
                        .replace("[PLAYER]", name)
                ));
                return;
            }});
            return;
        }

        TextUtil.sendTitle(player,
                plugin.getConfig().getString("messages.join.title")
                    .replace("[PLAYER]", name),
                plugin.getConfig().getString("messages.join.subtitle")
                    .replace("[PLAYER]", name)
        );
    }

    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
