package foxsgr.clandestinos.domain.model;

import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.util.Preconditions;

public class NeutralityRequest {

    private final String id;
    private final ClanTag requesterTag;
    private final ClanTag requesteeTag;

    public static final String ID_SEPARATOR = "$";

    public NeutralityRequest(ClanTag requesterTag, ClanTag requesteeTag) {
        Preconditions.ensureNotNull(requesterTag, "The requester clan cannot be null.");
        Preconditions.ensureNotNull(requesteeTag, "The requestee clan cannot be null.");
        this.requesterTag = requesterTag;
        this.requesteeTag = requesteeTag;
        id = this.requesterTag.withoutColor().value().toLowerCase() + ID_SEPARATOR
                + this.requesteeTag.withoutColor().value().toLowerCase();
    }

    public String id() {
        return id;
    }

    public ClanTag requester() {
        return requesterTag;
    }

    public ClanTag requestee() {
        return requesteeTag;
    }
}
