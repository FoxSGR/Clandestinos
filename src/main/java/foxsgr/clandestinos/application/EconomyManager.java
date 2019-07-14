package foxsgr.clandestinos.application;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyManager {

    private final JavaPlugin plugin;
    private final Economy economy;

    private static EconomyManager instance;

    private EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        economy = setupEconomy();

        if (economy == null) {
            throw new IllegalStateException("Could not setup economy. This plugin requires Vault.");
        }
    }

    static void init(JavaPlugin plugin) {
        instance = new EconomyManager(plugin);
    }

    private Economy setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
    }
}
