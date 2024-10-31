package hn.blacknight0981.craft.config;

import hn.blacknight0981.craft.enchants.EnchantioEnchant;
import hn.blacknight0981.craft.enchants.ExcavatingEnchant;
import hn.blacknight0981.craft.enchants.SmashingEnchant;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("UnstableApiUsage")
public class EnchantConfig {

    private ComponentLogger logger;

    public static final Map<Key, EnchantioEnchant> ENCHANTS = new HashMap<>();

    public EnchantConfig(Path filePath, ComponentLogger logger) throws IOException {
        this.logger = logger;
        File file = filePath.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        File configFile = new File(filePath.toFile(), "enchant_config.yml");
        configFile.createNewFile();

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);

        ConfigurationSection enchantsSection = configuration.getConfigurationSection("enchants");
        if (enchantsSection == null) {
            enchantsSection = configuration.createSection("enchants");
        }

        ConfigurationSection soulboundSection = enchantsSection.getConfigurationSection("soulbound");
        if (soulboundSection == null) {
            soulboundSection = enchantsSection.createSection("soulbound");
        }

        /*
            粉碎
        */
        ConfigurationSection smashingSection = enchantsSection.getConfigurationSection("smashing");
        if (smashingSection == null) {
            smashingSection = enchantsSection.createSection("smashing");
        }

        SmashingEnchant smashingEnchant = new SmashingEnchant(
                Config.getInt(smashingSection, "anvilCost", 1),
                Config.getInt(smashingSection, "weight", 5),
                Config.getInt(smashingSection, "maxLevel", 5),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        Config.getInt(smashingSection, "minimumCost.base", 15),
                        Config.getInt(smashingSection, "maximumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        Config.getInt(smashingSection, "maximumCost.base", 65),
                        Config.getInt(smashingSection, "maximumCost.additionalPerLevel", 1)
                ),
                Config.getBoolean(smashingSection, "canGetFromEnchantingTable", true),
                getTagsFromList(Config.getStringList(
                        smashingSection,
                        "supportedItemTags",
                        List.of(
                                "minecraft:wooden_pickaxe",
                                "minecraft:stone_pickaxe",
                                "minecraft:iron_pickaxe",
                                "minecraft:diamond_pickaxe",
                                "minecraft:golden_pickaxe",
                                "minecraft:netherite_pickaxe"
                        )
                ))
        );

        if (Config.getBoolean(smashingSection, "enabled", true)) {
            ENCHANTS.put(SmashingEnchant.KEY, smashingEnchant);
        }

        /*
            挖掘
        */
        ConfigurationSection excavatingSection = enchantsSection.getConfigurationSection("excavating");
        if (excavatingSection == null) {
            excavatingSection = enchantsSection.createSection("excavating");
        }

        ExcavatingEnchant excavatingEnchant = new ExcavatingEnchant(
                Config.getInt(excavatingSection, "anvilCost", 1),
                Config.getInt(excavatingSection, "weight", 1),
                Config.getInt(excavatingSection, "maxLevel", 2),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        Config.getInt(excavatingSection, "minimumCost.base", 20),
                        Config.getInt(excavatingSection, "maximumCost.additionalPerLevel", 3)
                ),
                EnchantmentRegistryEntry.EnchantmentCost.of(
                        Config.getInt(excavatingSection, "maximumCost.base", 65),
                        Config.getInt(excavatingSection, "maximumCost.additionalPerLevel", 1)
                ),
                Config.getBoolean(excavatingSection, "canGetFromEnchantingTable", true),
                getTagsFromList(Config.getStringList(
                        excavatingSection,
                        "supportedItemTags",
                        List.of(
                                "minecraft:wooden_pickaxe",
                                "minecraft:stone_pickaxe",
                                "minecraft:iron_pickaxe",
                                "minecraft:diamond_pickaxe",
                                "minecraft:golden_pickaxe",
                                "minecraft:netherite_pickaxe"
                        )
                ))
        );

        if (Config.getBoolean(excavatingSection, "enabled", true)) {
            ENCHANTS.put(ExcavatingEnchant.KEY, excavatingEnchant);
        }

        configuration.save(configFile);
    }

    private Set<TagEntry<ItemType>> getTagsFromList(@NotNull List<String> tags) {
        Set<TagEntry<ItemType>> supportedItemTags = new HashSet<>();
        for (String itemTag : tags) {
            if (itemTag == null) continue;
            if (itemTag.startsWith("#")) {
                itemTag = itemTag.substring(1);
                try {
                    Key key = Key.key(itemTag);
                    TagKey<ItemType> tagKey = ItemTypeTagKeys.create(key);
                    TagEntry<ItemType> tagEntry = TagEntry.tagEntry(tagKey);
                    supportedItemTags.add(tagEntry);
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage());
                }
                continue;
            }
            try {
                Key key = Key.key(itemTag);
                TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, key);
                TagEntry<ItemType> tagEntry = TagEntry.valueEntry(typedKey);
                supportedItemTags.add(tagEntry);
            } catch (IllegalArgumentException | NullPointerException e) {
                logger.error(e.getMessage());
            }
        }
        return supportedItemTags;
    }
}
