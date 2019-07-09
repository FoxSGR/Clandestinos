package foxsgr.clandestinos.application;

import clandestino.lib.Plugins;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Clandestinos extends JavaPlugin {

    private static final String CLAN_COMMAND = "clan";

    @Override
    public void onEnable() {
        ConfigManager.init(this);
        LanguageManager.init(this);
        PersistenceContext.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, new ClanCommand(this));
    }
}
