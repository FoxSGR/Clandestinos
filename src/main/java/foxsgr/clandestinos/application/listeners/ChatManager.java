package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Clandestinos;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
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
    private String clanTagFormat;
    private final Clandestinos plugin;

    private PlayerRepository playerRepository;

    public ChatManager(Clandestinos plugin) {
        this.plugin = plugin;
        // Must call setup to initialize other fields
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ClanPlayer clanPlayer = playerRepository.find(Finder.idFromPlayer(player));
        ClanTag clanTag;
        if (clanPlayer == null) {
            clanTag = null;
        } else {
            clanTag = clanPlayer.clan();
        }

        String formattedClanTag = "";
        if (clanTag != null) {
            formattedClanTag = clanTagFormat.replace(COLORED_CLAN_TAG_PLACEHOLDER, clanTag.value());
        }

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
        clanTagFormat = configManager.getString(ConfigManager.CLAN_FORMAT);

        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            throw new IllegalStateException("Could not setup chat. This plugin requires Vault");
        }

        chat = rsp.getProvider();
        playerRepository = PersistenceContext.repositories().players();
    }
}
