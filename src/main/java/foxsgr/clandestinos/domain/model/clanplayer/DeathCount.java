package foxsgr.clandestinos.domain.model.clanplayer;

import foxsgr.clandestinos.util.Preconditions;

public class DeathCount {

    private final Integer value;

    DeathCount(int value) {
        Preconditions.ensure(value >= 0, "The death count cannot be negative.");
        this.value = value;
    }

    DeathCount() {
        value = 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int value() {
        return value;
    }

    DeathCount increment() {
        return new DeathCount(value + 1);
    }
}
