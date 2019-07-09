package foxsgr.clandestinos.domain.model;

import clandestino.lib.Preconditions;
import foxsgr.clandestinos.domain.exceptions.AlreadyInClanException;

public class ClanPlayer {

    private final String id;
    private KillCount killCount;
    private ClanTag clanTag;

    public ClanPlayer(String id) {
        this.id = id;
        killCount = new KillCount();
        clanTag = null;
    }

    public ClanPlayer(String id, int killCount, ClanTag clanTag) {
        Preconditions.ensure(id != null, "Player ID cannot be null");
        this.id = id;
        this.killCount = new KillCount(killCount);
        this.clanTag = clanTag;
    }

    public String id() {
        return id;
    }

    public ClanTag clanTag() {
        return clanTag;
    }

    public int killCount() {
        return killCount.value();
    }

    public boolean inClan() {
        return clanTag != null;
    }

    public void changeClan(String tag) {
        if (clanTag != null) {
            throw new AlreadyInClanException();
        }

        clanTag = new ClanTag(tag);
    }
}
