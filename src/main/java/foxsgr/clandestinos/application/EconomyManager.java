package foxsgr.clandestinos.application;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
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

    public boolean take(Player player, double amount) {
        if (economy.withdrawPlayer(player, amount).transactionSuccess()) {
            if (amount != 0) {
                LanguageManager.send(player, LanguageManager.MONEY_TAKEN, format(amount));
            }

            return true;
        }

        return false;
    }

    public boolean hasEnough(Player player, double amount) {
        return economy.has(player, amount);
    }

    public String format(double amount) {
        return economy.format(amount);
    }

    public static EconomyManager getInstance() {
        return instance;
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
