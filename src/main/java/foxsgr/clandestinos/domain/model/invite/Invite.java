package foxsgr.clandestinos.domain.model.invite;

import clandestino.lib.Preconditions;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

import javax.persistence.*;

@Entity
public class Invite {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private final Clan invitedTo;

    @ManyToOne
    private final ClanPlayer invitedPlayer;

    @Enumerated(EnumType.STRING)
    private InviteResult inviteResult;

    public Invite(Clan invitedTo, ClanPlayer invitedPlayer) {
        Preconditions.ensureNotNull(invitedTo, "The clan that the player was invited to cannot be null.");
        Preconditions.ensureNotNull(invitedTo, "The invited player cannot be null.");
        this.invitedTo = invitedTo;
        this.invitedPlayer = invitedPlayer;
        inviteResult = InviteResult.PENDING;
    }

    /**
     * Creates the invite. For ORM only.
     */
    protected Invite() {
        // Gotta initialize final fields
        invitedTo = null;
        invitedPlayer = null;
    }
}
