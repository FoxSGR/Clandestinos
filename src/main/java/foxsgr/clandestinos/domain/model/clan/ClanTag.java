package foxsgr.clandestinos.domain.model.clan;

import clandestino.lib.Preconditions;
import clandestino.lib.TextUtil;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import org.bukkit.ChatColor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ClanTag {

    @Column(unique = true)
    private final String tagValue;

    public ClanTag(String tagValue) {
        tagValue = TextUtil.translateColoredText(tagValue);
        validate(tagValue);
        this.tagValue = tagValue;
    }

    /**
     * Creates the clan tag. For ORM only.
     */
    protected ClanTag() {
        tagValue = null;
    }

    public String value() {
        return tagValue;
    }

    public ClanTag withoutColor() {
        return new ClanTag(ChatColor.stripColor(tagValue));
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
