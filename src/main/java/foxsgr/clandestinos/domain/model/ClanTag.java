package foxsgr.clandestinos.domain.model;

import clandestino.lib.Preconditions;
import clandestino.lib.TextUtil;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import org.bukkit.ChatColor;

public class ClanTag {

    private final String tag;

    public ClanTag(String tag) {
        tag = TextUtil.translateColoredText(tag);
        validate(tag);
        this.tag = tag;
    }

    public String value() {
        return tag;
    }

    private void validate(String tag) {
        String withoutColor = ChatColor.stripColor(tag);
        if (!withoutColor.matches("[a-zA-Z]*")) {
            throw new NonLetterInTagException();
        }

        int length = withoutColor.length();
        int minLength = ConfigManager.getInt(ConfigManager.MIN_TAG_LENGTH);
        int maxLength = ConfigManager.getInt(ConfigManager.MAX_TAG_LENGTH);
        Preconditions.ensureBetween(length, minLength, maxLength, WrongTagSizeException.class);

        if (tag.contains(" ")) {
            throw new NonLetterInTagException();
        }
    }
}
