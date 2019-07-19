package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.listeners.ChatManager;
import foxsgr.clandestinos.application.listeners.DeathListener;
import foxsgr.clandestinos.application.listeners.JoinQuitListener;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Plugins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Clandestinos extends JavaPlugin {

    private boolean usingPAPI;

    private final ChatManager chatManager;
    private final JoinQuitListener joinQuitListener;
    private final DeathListener deathListener;

    private final ClanCommand clanCommand;
    private final ClanChatCommand clanChatCommand;

    private static final String CLAN_COMMAND = "clan";
    private static final String CLAN_CHAT_COMMAND = ".";

    public Clandestinos() {
        super();
        chatManager = new ChatManager(this);
        joinQuitListener = new JoinQuitListener();
        deathListener = new DeathListener();
        clanCommand = new ClanCommand(this);
        clanChatCommand = new ClanChatCommand();
    }

    @Override
    public void onEnable() {
        ClanLogger.init(this);
        ConfigManager.init(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            usingPAPI = true;
        }

        LanguageManager.init(this);
        PersistenceContext.init(this);

        // Must be after PersistenceContext.init(this)
        chatManager.setup();
        joinQuitListener.setup();
        deathListener.setup();

        clanChatCommand.setup();

        EconomyManager.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, clanCommand);
        Plugins.registerCommand(this, CLAN_CHAT_COMMAND, clanChatCommand);
        Bukkit.getPluginManager().registerEvents(chatManager, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);
    }

    boolean isUsingPAPI() {
        return usingPAPI;
    }
}
