package foxsgr.clandestinos.domain.services;

public class KDRDTO {

    public final int kills;
    public final int deaths;
    public final double kdr;

    public KDRDTO(int kills, int deaths, double kdr) {
        this.kills = kills;
        this.deaths = deaths;
        this.kdr = kdr;
    }
}
