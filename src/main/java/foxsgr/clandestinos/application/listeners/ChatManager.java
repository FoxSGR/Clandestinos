package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Clandestinos;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.TextUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ChatManager implements Listener {

    public static final String FORMATTED_CLAN_TAG_PLACEHOLDER = "%formatted_clan_tag%";
    public static final String COLORED_CLAN_TAG_PLACEHOLDER = "%colored_clan_tag%";
    public static final String PREFIX_PLACEHOLDER = "{prefix}";
    public static final String PLAYER_PLACEHOLDER = "{player}";
    public static final String CONTENT_PLACEHOLDER = "{content}";

    private Chat chat;
    private String format;
    private final Clandestinos plugin;

    private PlayerRepository playerRepository;

    public ChatManager(Clandestinos plugin) {
        this.plugin = plugin;
        // Must call setup to initialize other fields
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String formattedClanTag = formatClanTag(player);
        String prefix = chat.getPlayerPrefix(player);
        String messageFormat = format.replace(PREFIX_PLACEHOLDER, prefix)
                .replace(FORMATTED_CLAN_TAG_PLACEHOLDER, formattedClanTag)
                .replace(PLAYER_PLACEHOLDER, "%s")
                .replace(CONTENT_PLACEHOLDER, "%s");

        /*
        if (plugin.isUsingPAPI()) {
            // TODO: do some replacing
        }
        */

        messageFormat = TextUtil.translateColoredText(messageFormat);
        event.setFormat(messageFormat);

        if (player.hasPermission("essentials.chat.color")) {
            String message = TextUtil.translateColoredText(event.getMessage().replace("&k", ""));
            event.setMessage(message);
        }
    }

    public void setup() {
        ConfigManager configManager = ConfigManager.getInstance();
        format = configManager.getString(ConfigManager.CHAT_FORMAT);

        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            throw new IllegalStateException("Could not setup chat. This plugin requires Vault");
        }

        chat = rsp.getProvider();
        playerRepository = PersistenceContext.repositories().players();
    }

    private String formatClanTag(Player player) {
        ClanPlayer clanPlayer = playerRepository.find(Finder.idFromPlayer(player));
        if (clanPlayer == null || !clanPlayer.inClan()) {
            return "";
        }

        Clan clan = Finder.findClanEnsureExists(clanPlayer);
        String color;
        if (clan.isLeader(clanPlayer)) {
            color = plugin.getConfig().getString(ConfigManager.LEADER_DECORATION_COLOR);
        } else {
            color = plugin.getConfig().getString(ConfigManager.MEMBER_DECORATION_COLOR);
        }

        String left = plugin.getConfig().getString(ConfigManager.LEFT_OF_TAG);
        String right = plugin.getConfig().getString(ConfigManager.RIGHT_OF_TAG);
        return String.format("%s%s%s%s%s ", color, left, clan.tag(), color, right);
    }
}
