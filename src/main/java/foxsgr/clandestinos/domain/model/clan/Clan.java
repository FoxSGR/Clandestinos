package foxsgr.clandestinos.domain.model.clans.clan;

import clandestino.lib.Preconditions;
import foxsgr.clandestinos.domain.model.clans.clanplayer.ClanPlayer;

import javax.persistence.*;
import java.util.*;

@Entity
public class Clan {

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private ClanTag tag;

    @Embedded
    private ClanName name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ClanPlayer> leaders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<ClanPlayer> members;

    public Clan(String tag, String name, ClanPlayer creator) {
        Preconditions.ensureNotNull(creator, "The owner of a clan cannot be null.");

        this.tag = new ClanTag(tag);
        this.name = new ClanName(name);
        this.leaders = new LinkedHashSet<>();
        this.members = new LinkedHashSet<>();

        leaders.add(creator);
    }

    /**
     * Creates the clan. For ORM only.
     */
    protected Clan() {
        // Gotta initialize final fields
        leaders = new LinkedHashSet<>();
        members = new LinkedHashSet<>();
    }

    public ClanTag tag() {
        return tag;
    }

    public boolean addMember(ClanPlayer member) {
        return members.add(member);
    }

    public boolean isLeader(ClanPlayer player) {
        return leaders.contains(player);
    }
}
