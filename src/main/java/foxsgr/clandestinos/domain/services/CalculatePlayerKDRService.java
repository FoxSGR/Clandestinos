package foxsgr.clandestinos.domain.services;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

@SuppressWarnings("Duplicates")
public class CalculatePlayerKDRService {

    public KDRDTO calculatePlayerKDR(ClanPlayer player) {
        int kills = player.killCount().value();
        int deaths = player.deathCount().value();

        int dtoKills = kills;
        int dtoDeaths = deaths;

        if (kills == 0) {
            kills = 1;
        }

        if (deaths == 0) {
            deaths = 1;
        }

        return new KDRDTO(dtoKills, dtoDeaths, (double) kills / deaths);
    }
}
