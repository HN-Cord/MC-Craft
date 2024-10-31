package hn.blacknight0981.craft.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class Config {
    public static List<String> getStringList(ConfigurationSection section, String key, List<String> defaultValue) {
        List<String> list = section.contains(key) ? section.getStringList(key) : null;
        if (list == null) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return list;
    }

    public static int getInt(ConfigurationSection section, String key, int defaultValue) {
        int value = section.contains(key) ? section.getInt(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static double getDouble(ConfigurationSection section, String key, double defaultValue) {
        double value = section.contains(key) ? section.getDouble(key) : -1;
        if (value == -1) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static boolean getBoolean(ConfigurationSection section, String key, boolean defaultValue) {
        boolean value = section.contains(key) && section.getBoolean(key);
        if (!value) {
            section.set(key, defaultValue);
            return defaultValue;
        }
        return true;
    }
}
