package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.domain.exceptions.AlreadyInClanException;
import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.util.Preconditions;

import java.util.Objects;
import java.util.Optional;

public class ClanPlayer {

    public static final boolean DEFAULT_FRIENDLY_FIRE_ENABLED = true;

    private final String id;

    private boolean friendlyFireEnabled;
    private KillCount killCount;
    private DeathCount deathCount;
    private ClanTag clan;

    private transient KDR kdr; // Shouldn't be persisted

    public ClanPlayer(String id) {
        Preconditions.ensureNotEmpty(id, "The id of a player cannot be null or empty.");
        this.id = id;
        killCount = new KillCount();
        deathCount = new DeathCount();
        clan = null;
        kdr = new KDR(killCount, deathCount);
        friendlyFireEnabled = DEFAULT_FRIENDLY_FIRE_ENABLED;
    }

    public ClanPlayer(String id, int killCount, int deathCount, String clanTag, boolean friendlyFireEnabled) {
        Preconditions.ensureNotEmpty(id, "The id of a player cannot be null or empty.");
        this.id = id;
        this.killCount = new KillCount(killCount);
        this.deathCount = new DeathCount(deathCount);

        if (clanTag != null) {
            this.clan = new ClanTag(clanTag);
        }

        this.friendlyFireEnabled = friendlyFireEnabled;
        kdr = new KDR(this.killCount, this.deathCount);
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

    public Optional<ClanTag> clan() {
        return Optional.ofNullable(clan);
    }

    public KillCount killCount() {
        return killCount;
    }

    public DeathCount deathCount() {
        return deathCount;
    }

    public boolean isFriendlyFireEnabled() {
        return friendlyFireEnabled;
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

    public boolean toggleFriendlyFire() {
        friendlyFireEnabled = !friendlyFireEnabled;
        return friendlyFireEnabled;
    }

    public void incKillCount() {
        killCount = killCount.increment();
        kdr = new KDR(killCount, deathCount);
    }

    public void incDeathCount() {
        deathCount = deathCount.increment();
        kdr = new KDR(killCount, deathCount);
    }

    public KDR kdr() {
        return kdr;
    }
}
