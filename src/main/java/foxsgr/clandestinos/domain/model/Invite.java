package foxsgr.clandestinos.domain.model;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.Preconditions;

public class Invite {

    private final String id;
    private final ClanTag invitedTo;
    private final String invitedPlayer;

    public static final String ID_SEPARATOR = "$";

    public Invite(Clan invitedTo, ClanPlayer invitedPlayer) {
        Preconditions.ensureNotNull(invitedTo, "The clan that the player was invited to cannot be null.");
        Preconditions.ensureNotNull(invitedTo, "The invited player cannot be null.");
        this.id = invitedTo.simpleTag() + ID_SEPARATOR + invitedPlayer.id().toLowerCase();
        this.invitedTo = invitedTo.tag();
        this.invitedPlayer = invitedPlayer.id();
    }

    public Invite(String id, ClanTag invitedTo, String invitedPlayer) {
        this.id = id;
        this.invitedTo = invitedTo;
        this.invitedPlayer = invitedPlayer;
    }

    public String id() {
        return id;
    }

    public ClanTag invitedTo() {
        return invitedTo;
    }

    public String invitedPlayer() {
        return invitedPlayer;
    }
}
