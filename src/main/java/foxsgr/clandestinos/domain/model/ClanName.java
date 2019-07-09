package foxsgr.clandestinos.domain.model;

import clandestino.lib.Preconditions;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.domain.exceptions.WrongNameSizeException;
import org.bukkit.ChatColor;

@SuppressWarnings("WeakerAccess")
public class ClanName {

    private final String name;

    public ClanName(String name) {
        validate(name);
        this.name = name;
    }

    public String value() {
        return name;
    }

    private static void validate(String name) {
        int length = ChatColor.stripColor(name).length();
        int minLength = ConfigManager.getInt(ConfigManager.MIN_NAME_LENGTH);
        int maxLength = ConfigManager.getInt(ConfigManager.MAX_NAME_LENGTH);
        Preconditions.ensureBetween(length, minLength, maxLength, WrongNameSizeException.class);
    }
}
