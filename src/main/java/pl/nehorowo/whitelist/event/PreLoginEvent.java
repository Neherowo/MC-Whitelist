/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.nehorowo.whitelist.WhitelistPlugin;
import pl.nehorowo.whitelist.util.AntiBotUtil;
import pl.nehorowo.whitelist.util.TextUtil;

public class PreLoginEvent implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if(WhitelistPlugin.getInstance().getConfig().getBoolean("configuration.antibot.status")) {
            String name = event.getName();
            if (AntiBotUtil.isBot(name)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, TextUtil.fixColor(WhitelistPlugin.getInstance().getConfig().getString("messages.antibot.message")));
                TextUtil.sendLogger("Bot o nazwie &6" + name + "&e probowal wejsc na serwer. (L+ratio)", WhitelistPlugin.getInstance());
                return;
            }
            return;
        }
    }
}
