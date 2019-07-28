package foxsgr.clandestinos.domain.model.clan;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.exceptions.ChangeMoreThanColorsException;
import foxsgr.clandestinos.domain.exceptions.ChangeToSameTagException;
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
    private final Set<String> enemyClans;

    public Clan(String tag, String name, ClanPlayer owner) {
        this(tag, name, owner.id(), new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    public Clan(String tag, String name, String owner, Collection<String> leaders, Collection<String> members,
                Collection<String> enemyClans) {
        validate(owner, leaders, members, enemyClans);

        this.tag = new ClanTag(tag);
        this.name = new ClanName(name);

        this.owner = owner;
        this.leaders = new LinkedHashSet<>(leaders);
        this.members = new LinkedHashSet<>(members);
        this.leaders.add(owner); // IF this.leaders WAS A LIST THERE HAD TO BE A CONTAINS CHECK

        this.enemyClans = new LinkedHashSet<>(enemyClans);
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

    public String simpleTag() {
        return tag.withoutColor().value().toLowerCase();
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

    public List<String> enemyClans() {
        return new ArrayList<>(enemyClans);
    }

    public boolean isOwner(ClanPlayer player) {
        return owner.equals(player.id());
    }

    public boolean addMember(ClanPlayer member) {
        return members.add(member.id());
    }

    public boolean addEnemy(Clan clan) {
        return enemyClans.add(clan.simpleTag());
    }

    public void removeEnemy(Clan clan) {
        enemyClans.remove(clan.simpleTag());
    }

    public boolean isLeader(ClanPlayer player) {
        return leaders.contains(player.id());
    }

    public boolean isMember(ClanPlayer player) {
        return members.contains(player.id());
    }

    public boolean isEnemy(Clan clan) {
        return enemyClans.contains(clan.simpleTag());
    }

    public void remove(ClanPlayer player) {
        leaders.remove(player.id());
        members.remove(player.id());
    }

    public void makeLeader(ClanPlayer player){
        leaders.add(player.id());
        members.remove(player.id());
    }

    public void demoteLeader(ClanPlayer player){
        leaders.remove(player.id());
        members.add(player.id());
    }

    public void changeTag(String newTag) {
        String rawTag = TextUtil.stripColorAndFormatting(newTag);
        Preconditions.ensure(rawTag.equalsIgnoreCase(tag.withoutColor().value()), ChangeMoreThanColorsException.class);

        ClanTag newClanTag = new ClanTag(newTag);
        Preconditions.ensure(!newClanTag.equals(tag), ChangeToSameTagException.class);

        tag = newClanTag;
    }

    private static void validate(String owner, Collection<String> leaders, Collection<String> members, Collection<String> enemyClans) {
        Preconditions.ensureNotNull(owner, "The owner of a clan cannot be null.");

        for (String leader : leaders) {
            Preconditions.ensureNotNull(leader, "The leader of a clan cannot be null.");
        }

        for (String member : members) {
            Preconditions.ensureNotNull(member, "The leader of a clan cannot be null.");
        }

        for (String enemyClan : enemyClans) {
            Preconditions.ensureNotNull(enemyClan, "An enemy clan cannot be null.");
        }
    }

}
