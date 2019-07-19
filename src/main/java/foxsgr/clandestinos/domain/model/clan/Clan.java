package foxsgr.clandestinos.domain.model.clan;

import foxsgr.clandestinos.domain.exceptions.ChangeMoreThanColorsException;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.Preconditions;
import foxsgr.clandestinos.util.TextUtil;

import java.util.*;

public class Clan {

    private ClanTag tag;
    private ClanName name;
    private final String owner;
    private final Set<String> leaders;
    private final Set<String> members;

    public Clan(String tag, String name, ClanPlayer owner) {
        this(tag, name, owner.id(), new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    public Clan(String tag, String name, String owner, Collection<String> leaders, Collection<String> members) {
        Preconditions.ensureNotNull(owner, "The owner of a clan cannot be null.");

        this.tag = new ClanTag(tag);
        this.name = new ClanName(name);
        this.owner = owner;
        this.leaders = new LinkedHashSet<>(leaders);
        this.members = new LinkedHashSet<>(members);

        this.leaders.add(owner); // IF this.leaders WAS A LIST THERE HAD TO BE A CONTAINS CHECK
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }

        if (!(otherObject instanceof Clan)) {
            return false;
        }

        Clan otherClan = (Clan) otherObject;
        return tag.withoutColor().value().equalsIgnoreCase(otherClan.tag.withoutColor().value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag.withoutColor().value().toLowerCase());
    }

    public ClanTag tag() {
        return tag;
    }

    public ClanName name() {
        return name;
    }

    public String owner() {
        return owner;
    }

    public List<String> allPlayers() {
        List<String> all = new ArrayList<>(leaders);
        all.addAll(members);
        return all;
    }

    public List<String> leaders() {
        return new ArrayList<>(leaders);
    }

    public List<String> members() {
        return new ArrayList<>(members);
    }

    public boolean isOwner(ClanPlayer player) {
        return owner.equals(player.id());
    }

    public boolean addMember(ClanPlayer member) {
        return members.add(member.id());
    }

    public boolean isLeader(ClanPlayer player) {
        return leaders.contains(player.id());
    }

    public boolean isMember(ClanPlayer player) {
        return members.contains(player.id());
    }

    public void remove(ClanPlayer player) {
        leaders.remove(player.id());
        members.remove(player.id());
    }

    public void changeTag(String newTag) {
        String rawTag = TextUtil.stripColorAndFormatting(newTag);
        Preconditions.ensure(rawTag.equalsIgnoreCase(tag.withoutColor().value()), ChangeMoreThanColorsException.class);
        tag = new ClanTag(newTag);
    }
}
