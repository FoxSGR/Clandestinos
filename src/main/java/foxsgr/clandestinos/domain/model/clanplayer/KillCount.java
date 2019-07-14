package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.util.Preconditions;

public class KillCount {

    private Integer value;

    public KillCount(int value) {
        Preconditions.ensure(value >= 0, "The kill count cannot be negative.");
        this.value = value;
    }

    public KillCount() {
        value = 0;
    }

    public int value() {
        return value;
    }
}
