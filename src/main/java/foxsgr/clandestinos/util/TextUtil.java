package foxsgr.clandestinos.util;

import org.bukkit.ChatColor;

public final class TextUtil {

    private TextUtil() {

    }

    public static String translateColoredText(String text) {
        text = text.replace('§', '&');
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
