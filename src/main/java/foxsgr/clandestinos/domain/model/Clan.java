package foxsgr.clandestinos.domain.model;

import clandestino.lib.Preconditions;
import clandestino.lib.TextUtil;
import org.bukkit.ChatColor;

import java.util.*;

public class Clan {

    private ClanTag tag;
    private ClanTag coloredTag;
    private ClanName name;
    private String ownerId;
    private Set<String> members;

    public Clan(String tagWithColor, String name, String ownerId, List<String> members) {
        Preconditions.ensureNotEmpty(ownerId, "The owner of a clan cannot be null or empty.");

        tagWithColor = TextUtil.translateColoredText(tagWithColor);
        this.tag = new ClanTag(ChatColor.stripColor(tagWithColor));
        coloredTag = new ClanTag(tagWithColor);
        this.name = new ClanName(name);
        this.ownerId = ownerId;
        this.members = new LinkedHashSet<>(members);
    }

    public Clan(String tagWithColor, String name, String ownerId) {
        this(tagWithColor, name, ownerId, new ArrayList<>());
    }

    public ClanTag tag() {
        return tag;
    }

    public ClanTag coloredTag() {
        return coloredTag;
    }

    public ClanName name() {
        return name;
    }

    public String ownerId() {
        return ownerId;
    }

    public Set<String> members() {
        return new HashSet<>(members);
    }

    public void addMember(String player) {
        this.members.add(player);
    }
}
