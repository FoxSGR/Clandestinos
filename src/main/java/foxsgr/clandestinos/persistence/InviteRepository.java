package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.invite.Invite;

public interface InviteRepository {

    Invite save(Invite invite);
    Invite findPending(String invited, String clanTag);
}
