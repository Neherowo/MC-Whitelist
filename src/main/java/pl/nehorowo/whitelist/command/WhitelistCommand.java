/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */


package pl.nehorowo.whitelist.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.nehorowo.whitelist.WhitelistPlugin;
import pl.nehorowo.whitelist.util.TextUtil;
import pl.nehorowo.whitelist.util.WhitelistUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WhitelistCommand implements CommandExecutor, TabCompleter {

    private final Plugin plugin;

    public WhitelistCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    //arguments
    private static final List<String> arguments = Arrays.asList("reload", "add", "remove", "list", "on", "off", "status");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 0) {
            TextUtil.sendUsage(sender, "/whitelist <reload|add|remove|list|on|off|status|check> <nick>");
            return true;
        }
        switch (args[0]) {
            case "reload": {
                if (!(args.length == 1)) {
                    TextUtil.sendUsage(sender, "/whitelist reload");
                    return true;
                }
                long a = System.currentTimeMillis();

                WhitelistPlugin.getInstance().saveConfig();

                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.whitelist-reload")).replace(
                            "[TIME]", (System.currentTimeMillis() - a) + "ms")
                );
                break;
            }

            case "add": {
                if(!(args.length == 2)) {
                    TextUtil.sendUsage(sender, "/whitelist add <nick>");
                    return true;
                }
                String name = args[1];
                //if alr whitelisted
                if(WhitelistUtil.isWhitelisted(name)) {
                    TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.already-whitelisted"))
                            .replace("[TARGET]", args[1]
                    ));
                    return true;
                }
                //add
                WhitelistUtil.addWhitelist(name);
                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.added-player"))
                        .replace("[TARGET]", args[1]
                ));
                break;
            }

            case "remove": {
                //if args <  2 or smth
                if(!(args.length == 2)) {
                    TextUtil.sendUsage(sender, "/whitelist remove <nick>");
                    return true;
                }

                String name = args[1];
                //if not whitelisted
                if(!WhitelistUtil.isWhitelisted(name)) {
                    TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.already-not-whitelisted")).replace(
                            "[TARGET]", args[1]
                    ));
                    return true;
                }

                //remove
                WhitelistUtil.removeWhitelist(name);

                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.removed-player")).replace(
                        "[TARGET]", args[1]
                ));
                break;
            }

            case "list": {
                if(!(args.length == 1)) {
                    TextUtil.sendUsage(sender, "/whitelist list");
                    return true;
                }
                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.list.message")));
                Objects.requireNonNull(plugin.getConfig().getStringList("configuration.whitelist.list")).forEach(whitelisted -> {
                    TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.list.format"))
                            .replace("[LIST]", whitelisted));
                });
                break;
            }

            case "on": {
                if(!(args.length == 1)) {
                    TextUtil.sendUsage(sender, "/whitelist on");
                    return true;
                }
                if(plugin.getConfig().getBoolean("configuration.whitelist.status")) {
                    TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.already-enabled"))
                            .replace("[STATUS]", "&aWLACZONA"));
                    return true;
                }


                plugin.getConfig().set("configuration.whitelist.status", true);
                WhitelistPlugin.getInstance().saveConfig();


                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.changed-whitelist-status"))
                        .replace("[STATUS]", "&aWLACZONA"));
                break;
            }

            case "off": {
                if(!(args.length == 1)) {
                    TextUtil.sendUsage(sender, "/whitelist off");
                    return true;
                }
                if(!plugin.getConfig().getBoolean("configuration.whitelist.status")) {
                    TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.already-disabled"))
                            .replace("[STATUS]", "&cWYLACZONA"));
                    return true;
                }

                plugin.getConfig().set("configuration.whitelist.status", false);
                WhitelistPlugin.getInstance().saveConfig();


                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.changed-whitelist-status"))
                        .replace("[STATUS]", "&cWYLACZONA"));
                break;
            }

            case "status": {
                if(!(args.length == 1)) {
                    TextUtil.sendUsage(sender, "/whitelist status");
                    return true;
                }
                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.whitelist-status"))
                        .replace("[STATUS]", plugin.getConfig().getBoolean("configuration.whitelist.status") ? "&aWLACZONA" : "&cWYLACZONA"));
                break;
            }

            case "check": {
                if(!(args.length == 2)) {
                    TextUtil.sendUsage(sender, "/whitelist check <nick>");
                    return true;
                }
                TextUtil.sendMessage(sender, Objects.requireNonNull(plugin.getConfig().getString("messages.whitelist.check-message"))
                        .replace("[TARGET]", args[1])
                        .replace("[STATUS]", WhitelistUtil.isWhitelisted(args[1]) ? "&cnie posiada dostepu do whitelist." : "&aposiada dostep do whitelist.")
                );

            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            //when someone write wrong first argument (for example: /whitelist ADD -> /whitelist add)
            for (String arg : arguments) if (arg.startsWith(args[0].toLowerCase())) results.add(arg);
            return results;
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("remove")) plugin.getConfig().getStringList("configuration.whitelist.list").forEach(player -> { if (player.startsWith(args[1].toLowerCase())) results.add(player);});
            else if(args[0].equalsIgnoreCase("add")) plugin.getServer().getOnlinePlayers().forEach(player -> { if (player.getName().startsWith(args[1].toLowerCase())) results.add(player.getName());});
            else if(args[0].equalsIgnoreCase("check")) plugin.getServer().getOnlinePlayers().forEach(player -> { if (player.getName().startsWith(args[1].toLowerCase())) results.add(player.getName());});
            plugin.getConfig().getStringList("configuration.whitelist.list").forEach(player -> { if (player.startsWith(args[1].toLowerCase())) results.add(player);});
        }
        return results;
    }
}
