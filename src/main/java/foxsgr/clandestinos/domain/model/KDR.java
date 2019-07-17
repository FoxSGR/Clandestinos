package foxsgr.clandestinos.domain.model;

import foxsgr.clandestinos.domain.model.clanplayer.DeathCount;
import foxsgr.clandestinos.domain.model.clanplayer.KillCount;

public class KDR {

    private final int kills;
    private final int deaths;
    private final double kdrValue;

    public KDR(KillCount killCount, DeathCount deathCount) {
        kills = killCount.value();
        deaths = deathCount.value();
        kdrValue = calculateKDR();
    }

    public KDR(int kills, int deaths, double kdrValue) {
        this.kills = kills;
        this.deaths = deaths;
        this.kdrValue = kdrValue;
    }

    private double calculateKDR() {
        double kdrKills = kills == 0 ? 1 : kills;
        int kdrDeaths = deaths == 0 ? 1 : deaths;
        return kdrKills / kdrDeaths;
    }

    public int kills() {
        return kills;
    }

    public int deaths() {
        return deaths;
    }

    public double kdr() {
        return kdrValue;
    }
}
