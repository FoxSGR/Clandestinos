package foxsgr.clandestinos.domain.model.clan;

import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.domain.exceptions.WrongNameSizeException;
import foxsgr.clandestinos.util.Preconditions;
import org.bukkit.ChatColor;

public class ClanName {

    private final String name;

    ClanName(String name) {
        validate(name);
        this.name = name;
    }

    public String value() {
        return name;
    }

    private static void validate(String name) {
        int length = ChatColor.stripColor(name).length();
        int minLength = ConfigManager.getInstance().getInt(ConfigManager.MIN_NAME_LENGTH);
        int maxLength = ConfigManager.getInstance().getInt(ConfigManager.MAX_NAME_LENGTH);
        Preconditions.ensureBetween(length, minLength, maxLength, WrongNameSizeException.class);
    }
}
