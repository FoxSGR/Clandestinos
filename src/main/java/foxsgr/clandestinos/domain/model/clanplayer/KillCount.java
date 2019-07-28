package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.util.Preconditions;

public class KillCount {

    private Integer value;

    KillCount(int value) {
        Preconditions.ensure(value >= 0, "The kill count cannot be negative.");
        this.value = value;
    }

    KillCount() {
        value = 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int value() {
        return value;
    }

    public KillCount increment() {
        return new KillCount(value + 1);
    }
}
