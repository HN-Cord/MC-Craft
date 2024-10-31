package hn.blacknight0981.craft;

import hn.blacknight0981.craft.config.EnchantConfig;
import hn.blacknight0981.craft.enchants.ExcavatingEnchant;
import hn.blacknight0981.craft.listeners.ExcavatingListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Craft extends JavaPlugin {

    @Override
    public void onEnable() {
        initEnchantListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initEnchantListeners() {
        getLogger().info("註冊監聽器...");
        if (EnchantConfig.ENCHANTS.containsKey(ExcavatingEnchant.KEY)) {
            getServer().getPluginManager().registerEvents(new ExcavatingListener(), this);
        }
    }
}
