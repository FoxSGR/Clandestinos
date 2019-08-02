package foxsgr.clandestinos.domain.model;

import foxsgr.clandestinos.domain.model.clanplayer.DeathCount;
import foxsgr.clandestinos.domain.model.clanplayer.KillCount;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KDR implements Comparable<KDR> {

    private final int kills;
    private final int deaths;
    private final double kdrValue;

    public KDR(KillCount killCount, DeathCount deathCount) {
        kills = killCount.value();
        deaths = deathCount.value();
        kdrValue = calculateKDR();
    }

    public KDR(int killCount, int deathCount) {
        kills = killCount;
        deaths = deathCount;
        kdrValue = calculateKDR();
    }

    public KDR(int kills, int deaths, double kdrValue) {
        this.kills = kills;
        this.deaths = deaths;
        this.kdrValue = kdrValue;
    }

    @Override
    public String toString() {
        return String.format("%.2f", kdrValue);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof KDR)) {
            return false;
        }

        KDR otherKDR = (KDR) other;
        return Double.compare(otherKDR.kdrValue, kdrValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kdrValue);
    }

    @Override
    public int compareTo(@NotNull KDR other) {
        return -Double.compare(kdrValue, other.kdrValue);
    }

    private double calculateKDR() {
        if (kills == 0 && deaths == 1) {
            return 0;
        }

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

    public KDR subtract(KillCount killCount, DeathCount deathCount) {
        return new KDR(kills - killCount.value(), deaths - deathCount.value());
    }

    public KDR addKills(int amount) {
        return new KDR(kills + amount, deaths);
    }

    public KDR addDeaths(int amount) {
        return new KDR(kills, deaths + amount);
    }
}
