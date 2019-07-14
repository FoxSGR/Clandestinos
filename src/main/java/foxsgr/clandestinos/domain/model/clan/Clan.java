package foxsgr.clandestinos.domain.model.clan;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.Preconditions;

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

    public ClanTag tag() {
        return tag;
    }

    public ClanName name() {
        return name;
    }

    public String owner() {
        return owner;
    }

    public List<String> leaders() {
        return new ArrayList<>(leaders);
    }

    public List<String> members() {
        return new ArrayList<>(members);
    }

    public boolean isOwner(String id) {
        return owner.equals(id);
    }

    public boolean addMember(ClanPlayer member) {
        return members.add(member.id());
    }

    public boolean isLeader(ClanPlayer player) {
        return leaders.contains(player.id());
    }

    public void remove(ClanPlayer clanPlayer) {
        leaders.remove(clanPlayer.id());
        members.remove(clanPlayer.id());
    }
}
