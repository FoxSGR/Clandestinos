package foxsgr.clandestinos.domain.model.clanplayer;

import clandestino.lib.Preconditions;
import foxsgr.clandestinos.domain.exceptions.AlreadyInClanException;
import foxsgr.clandestinos.domain.model.clan.Clan;

import javax.persistence.*;

@Entity
public class ClanPlayer {

    @Id
    private final String id;

    @Embedded
    private KillCount killCount;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Clan clan;

    public ClanPlayer(String id) {
        this.id = id;
        killCount = new KillCount();
        clan = null;
    }

    protected ClanPlayer() {
        id = null; // gotta initialize final field
    }

    public String id() {
        return id;
    }

    public Clan clan() {
        return clan;
    }

    public boolean inClan() {
        return clan != null;
    }

    public void joinClan(Clan clanToJoin) {
        if (clan != null) {
            throw new AlreadyInClanException();
        }

        Preconditions.ensureNotNull(clanToJoin, "The clan to join cannot be null.");
        clan = clanToJoin;
    }
}
