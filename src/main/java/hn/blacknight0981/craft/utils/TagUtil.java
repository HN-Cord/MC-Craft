package hn.blacknight0981.craft.utils;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.TagEntry;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagUtil {
    public static Set<TagEntry<ItemType>> getTagsFromList(@NotNull List<String> tags) {
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
                    e.printStackTrace();
                }
                continue;
            }
            try {
                Key key = Key.key(itemTag);
                TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, key);
                TagEntry<ItemType> tagEntry = TagEntry.valueEntry(typedKey);
                supportedItemTags.add(tagEntry);
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return supportedItemTags;
    }
}
