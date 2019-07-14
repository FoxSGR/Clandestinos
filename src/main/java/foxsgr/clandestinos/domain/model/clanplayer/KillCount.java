package foxsgr.clandestinos.domain.model.clans.clanplayer;

import javax.persistence.Embeddable;

@Embeddable
public class KillCount {

    private Integer value;

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
