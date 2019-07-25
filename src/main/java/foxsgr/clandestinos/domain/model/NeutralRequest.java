package foxsgr.clandestinos.domain.model;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.util.Preconditions;

public class NeutralRequest {

    private final ClanTag requesterTag;
    private final ClanTag requesteeTag;

    public NeutralRequest(Clan requester, Clan requestee) {
        Preconditions.ensureNotNull(requester, "The requester clan cannot be null.");
        Preconditions.ensureNotNull(requestee, "The requestee clan cannot be null.");
        requesterTag = requester.tag();
        requesteeTag = requestee.tag();
    }

    public ClanTag requester() {
        return requesterTag;
    }

    public ClanTag requestee() {
        return requesteeTag;
    }
}
