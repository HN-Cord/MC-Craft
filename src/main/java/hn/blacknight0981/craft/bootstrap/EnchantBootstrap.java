package hn.blacknight0981.craft.bootstrap;

import hn.blacknight0981.craft.config.EnchantConfig;
import hn.blacknight0981.craft.enchants.EnchantioEnchant;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class EnchantBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        try {
            new EnchantConfig(context.getDataDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collection<EnchantioEnchant> enchantioEnchants = EnchantConfig.ENCHANTS.values();

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM).newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                context.getLogger().info("註冊物品標籤 : {}", enchant.getTagForSupportedItems().key());
                event.registrar().addToTag(
                        ItemTypeTagKeys.create(enchant.getTagForSupportedItems().key()),
                        enchant.getSupportedItems()
                );
            }
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                context.getLogger().info("註冊附魔 : {}", enchant.getKey());
                event.registry().register(TypedKey.create(RegistryKey.ENCHANTMENT, enchant.getKey()), (enchantment) -> {
                    enchantment.description(enchant.getDescription());
                    enchantment.anvilCost(enchant.getAnvilCost());
                    enchantment.maxLevel(enchant.getMaxLevel());
                    enchantment.weight(enchant.getWeight());
                    enchantment.minimumCost(enchant.getMinimumCost());
                    enchantment.maximumCost(enchant.getMaximumCost());
                    enchantment.activeSlots(enchant.getActiveSlots());
                    enchantment.supportedItems(event.getOrCreateTag(enchant.getTagForSupportedItems()));
                });
            }
        }));

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT).newHandler((event) -> {
            for (EnchantioEnchant enchant : enchantioEnchants) {
                enchant.getEnchantTagKeys().forEach(enchantTagKey -> {
                    context.getLogger().info("註冊附魔標籤 : {}", enchantTagKey.key());
                    event.registrar().addToTag(enchantTagKey, Set.of(enchant.getTagEntry()));
                });
            }
        }));
    }
}
