package foxsgr.clandestinos.domain.model;

public class KillCount {

    private int value;

    public KillCount(int value) {
        this.value = value;
    }

    public KillCount() {
        value = 0;
    }

    public int value() {
        return value;
    }
}
