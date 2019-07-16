package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.util.Preconditions;

public class DeathCount {

    private final Integer value;

    public DeathCount(int value) {
        Preconditions.ensure(value >= 0, "The death count cannot be negative.");
        this.value = value;
    }

    public DeathCount() {
        value = 0;
    }

    public int value() {
        return value;
    }

    public DeathCount increment() {
        return new DeathCount(value + 1);
    }
}
