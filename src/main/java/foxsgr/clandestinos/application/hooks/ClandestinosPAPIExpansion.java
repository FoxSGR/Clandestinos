package foxsgr.clandestinos.application.hooks;

import foxsgr.clandestinos.application.Clandestinos;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.TextUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ClandestinosPAPIExpansion extends PlaceholderExpansion {

    private final Clandestinos clandestinos;
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    private static final String KILLS_PLACEHOLDER = "kills";
    private static final String DEATHS_PLACEHOLDER = "deaths";
    private static final String KDR_PLACEHOLDER = "kdr";
    private static final String FORMATTED_CLAN_TAG_PLACEHOLDER = "formatted_tag";
    private static final String COLORED_CLAN_TAG_PLACEHOLDER = "colored_tag";

    public ClandestinosPAPIExpansion(Clandestinos clandestinos) {
        this.clandestinos = clandestinos;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "FoxSGR";
    }

    @Override
    public String getIdentifier() {
        return clandestinos.getName().toLowerCase();
    }

    @Override
    public String getVersion() {
        return clandestinos.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        return onRequest(player, identifier);
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null || clandestinos == null) {
            return "";
        }

        String replaced = identify(player, identifier);
        if (replaced != null) {
            replaced = TextUtil.translateColoredText(replaced);
        }

        return replaced;
    }

    private String identify(OfflinePlayer player, String identifier) {
        Optional<ClanPlayer> clanPlayer;
        switch (identifier) {
            case KILLS_PLACEHOLDER:
                return findPlayer(player).map(value -> value.killCount().toString()).orElse("0");
            case DEATHS_PLACEHOLDER:
                return findPlayer(player).map(value -> value.deathCount().toString()).orElse("0");
            case KDR_PLACEHOLDER:
                return findPlayer(player).map(value -> String.format("%.2f", value.kdr().kdr())).orElse("1.00");
            case FORMATTED_CLAN_TAG_PLACEHOLDER:
                return clandestinos.chatManager().formatClanTag(player);
            case COLORED_CLAN_TAG_PLACEHOLDER:
                clanPlayer = findPlayer(player);
                return clanPlayer.map(this::coloredClanTag).orElse("");
            default:
                return null;
        }
    }

    private Optional<ClanPlayer> findPlayer(OfflinePlayer player) {
        return Optional.ofNullable(Finder.findPlayer(player));
    }

    private String coloredClanTag(@NotNull ClanPlayer clanPlayer) {
        Clan clan = clanPlayer.clan().map(tag -> clanRepository.findByTag(tag.withoutColor().value())).orElse(null);
        if (clan == null) {
            return "";
        } else {
            return clan.tag().value();
        }
    }
}
