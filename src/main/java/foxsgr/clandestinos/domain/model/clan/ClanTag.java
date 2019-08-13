package foxsgr.clandestinos.domain.model.clan;

import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import foxsgr.clandestinos.util.Preconditions;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ClanTag {

    private final String tagValue;

    public ClanTag(String tagValue) {
        Preconditions.ensureNotNull(tagValue, "The value of a clan tag cannot be null.");
        tagValue = TextUtil.translateColoredText(tagValue);
        validate(tagValue);

        this.tagValue = tagValue;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ClanTag)) {
            return false;
        }

        ClanTag otherTag = (ClanTag) object;
        return tagValue.equals(otherTag.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagValue);
    }

    @Override
    public String toString() {
        return tagValue;
    }

    public boolean equalsIgnoreColor(@Nullable ClanTag otherClanTag) {
        if (otherClanTag == null) {
            return false;
        }

        return withoutColor().value().equalsIgnoreCase(otherClanTag.withoutColor().value());
    }

    public String value() {
        return tagValue;
    }

    public ClanTag withoutColor() {
        return new ClanTag(ChatColor.stripColor(tagValue));
    }

    private void validate(String tag) {
        if (TextUtil.containsFormattingCodes(tag)) {
            throw new NonLetterInTagException();
        }

        String withoutColor = ChatColor.stripColor(tag);
        if (!withoutColor.matches("[a-zA-Z]*")) {
            throw new NonLetterInTagException();
        }

        int length = withoutColor.length();
        int minLength = ConfigManager.getInstance().getInt(ConfigManager.MIN_TAG_LENGTH);
        int maxLength = ConfigManager.getInstance().getInt(ConfigManager.MAX_TAG_LENGTH);
        Preconditions.ensureBetween(length, minLength, maxLength, WrongTagSizeException.class);

        if (tag.contains(" ")) {
            throw new NonLetterInTagException();
        }
    }
}
