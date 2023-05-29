/*

   Project: MC-Whitelist
   @Since 29.05.2023

 */
package pl.nehorowo.whitelist.util;

public class AntiBotUtil {

    public static boolean isBot(String name) {
        return name.startsWith("MCSTORM") || name.contains("MC_STORM") || name.contains("MCSTORM_IO");
    }

}
