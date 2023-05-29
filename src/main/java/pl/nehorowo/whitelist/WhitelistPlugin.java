/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.nehorowo.whitelist.command.WhitelistCommand;
import pl.nehorowo.whitelist.event.LoginEvent;
import pl.nehorowo.whitelist.event.PreLoginEvent;
import pl.nehorowo.whitelist.util.TextUtil;

public class WhitelistPlugin extends JavaPlugin {

    @Getter public static WhitelistPlugin instance;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        TextUtil.sendLogger("Wlaczanie pluginu...", this);
        instance = this;

        saveDefaultConfig();
        TextUtil.sendLogger("Zaladowano plik konfiguracyjny #1 w &6" + (System.currentTimeMillis() - start) + "ms", this);

        getCommand("whitelist").setExecutor(new WhitelistCommand(this));
        TextUtil.sendLogger("Zaladowano komende(y) w &6" + (System.currentTimeMillis() - start) + "ms", this);

        Bukkit.getPluginManager().registerEvents(new PreLoginEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LoginEvent(this), this);
        TextUtil.sendLogger("Zaladowano event(y) w &6" + (System.currentTimeMillis() - start) + "ms", this);

        TextUtil.sendLogger("Wlaczono plugin w &6" + (System.currentTimeMillis() - start) + "ms", this);
    }
}
