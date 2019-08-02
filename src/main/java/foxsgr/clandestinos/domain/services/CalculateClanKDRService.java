package foxsgr.clandestinos.domain.services;

import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

public class CalculateClanKDRService {

    public KDR calculateClanKDR(Clan clan, Iterable<ClanPlayer> players) {
        int kills = 0;
        int deaths = 0;

        for (ClanPlayer player : players) {
            kills += player.killCount().value();
            deaths += player.deathCount().value();
        }

        int dtoKills = kills;
        int dtoDeaths = deaths;

        if (deaths == 0) {
            deaths = 1;
        }

        if (kills == 0) {
            kills = 1;
        }

        KDR kdr = new KDR(dtoKills, dtoDeaths, (double) kills / deaths);
        clan.updateKDR(kdr);
        return kdr;
    }
}
