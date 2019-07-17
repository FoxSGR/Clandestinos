package foxsgr.clandestinos.domain.services;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

@SuppressWarnings("Duplicates")
public class CalculateClanKDRService {

    public KDRDTO calculateClanKDR(Iterable<ClanPlayer> players) {
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

        return new KDRDTO(dtoKills, dtoDeaths, (double) kills / deaths);
    }
}
