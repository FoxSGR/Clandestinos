package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Clandestinos;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ChatManager implements Listener {

    public static final String FORMATTED_CLAN_TAG_PLACEHOLDER = "%clandestinos_formatted_tag%";
    public static final String COLORED_CLAN_TAG_PLACEHOLDER = "%clandestinos_colored_tag%";
    public static final String PREFIX_PLACEHOLDER = "{prefix}";
    public static final String PLAYER_PLACEHOLDER = "{player}";
    public static final String CONTENT_PLACEHOLDER = "{content}";

    private Chat chat;
    private String format;
    private String leaderDecoColor;
    private String memberDecoColor;
    private String leftOfTag;
    private String rightOfTag;
    private final Clandestinos plugin;

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

        messageFormat = TextUtil.translateColoredText(messageFormat);
        if (plugin.isUsingPAPI()) {
            messageFormat = PlaceholderAPI.setPlaceholders(player, messageFormat);
        }

        event.setFormat(messageFormat);

        if (player.hasPermission("essentials.chat.color")) {
            String message = TextUtil.translateColoredText(event.getMessage().replace("&k", ""));
            event.setMessage(message);
        }
    }

    public void setup() {
        ConfigManager configManager = ConfigManager.getInstance();
        format = configManager.getString(ConfigManager.CHAT_FORMAT);
        leaderDecoColor = configManager.getString(ConfigManager.LEADER_DECORATION_COLOR);
        memberDecoColor = configManager.getString(ConfigManager.MEMBER_DECORATION_COLOR);
        rightOfTag = configManager.getString(ConfigManager.RIGHT_OF_TAG);
        leftOfTag = configManager.getString(ConfigManager.LEFT_OF_TAG);

        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            throw new IllegalStateException("Could not setup chat. This plugin requires Vault");
        }

        chat = rsp.getProvider();
    }

    public String formatClanTag(OfflinePlayer player) {
        ClanPlayer clanPlayer = Finder.findPlayer(player);
        if (clanPlayer == null || !clanPlayer.inClan()) {
            return "";
        }

        Clan clan = Finder.findClanEnsureExists(clanPlayer);
        String color;
        if (clan.isLeader(clanPlayer)) {
            color = leaderDecoColor;
        } else {
            color = memberDecoColor;
        }

        return String.format("%s%s%s%s%s %s", color, leftOfTag, clan.tag(), color, rightOfTag, ChatColor.RESET);
    }
}
