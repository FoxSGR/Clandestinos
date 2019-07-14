package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.Invite;

public interface InviteRepository {

    void add(Invite invite);
    Invite find(String invited, String clanTag);
}
