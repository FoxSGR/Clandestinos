package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.domain.exceptions.AlreadyInClanException;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.util.Preconditions;

import java.util.Objects;

public class ClanPlayer {

    private final String id;
    private KillCount killCount;
    private DeathCount deathCount;
    private ClanTag clan;

    public ClanPlayer(String id) {
        Preconditions.ensureNotEmpty(id, "The id of a player cannot be null or empty.");
        this.id = id;
        killCount = new KillCount();
        deathCount = new DeathCount();
        clan = null;
    }

    public ClanPlayer(String id, int killCount, int deathCount, String clanTag) {
        Preconditions.ensureNotEmpty(id, "The id of a player cannot be null or empty.");
        this.id = id;
        this.killCount = new KillCount(killCount);
        this.deathCount = new DeathCount(deathCount);

        if (clanTag != null) {
            this.clan = new ClanTag(clanTag);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ClanPlayer)) {
            return false;
        }

        ClanPlayer otherPlayer = (ClanPlayer) object;
        return id.equals(otherPlayer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String id() {
        return id;
    }

    public ClanTag clan() {
        return clan;
    }

    public KillCount killCount() {
        return killCount;
    }

    public DeathCount deathCount() {
        return deathCount;
    }

    public boolean inClan() {
        return clan != null;
    }

    public void joinClan(Clan clanToJoin) {
        if (clan != null) {
            throw new AlreadyInClanException();
        }

        Preconditions.ensureNotNull(clanToJoin, "The clan to join cannot be null.");
        clan = clanToJoin.tag();
    }

    public void leaveClan() {
        clan = null;
    }

    public void incKillCount() {
        killCount = killCount.increment();
    }

    public void incDeathCount() {
        deathCount = deathCount.increment();
    }
}
