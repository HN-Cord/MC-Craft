package hn.blacknight0981.craft.listeners;

import hn.blacknight0981.craft.enchants.SmashingEnchant;
import hn.blacknight0981.craft.model.material.SmashingMaterial;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SmashingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment excavating = registry.get(SmashingEnchant.KEY);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        if (excavating == null) return;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(excavating)) return;

        Block block = event.getBlock();
        Material originalMaterial = block.getType();

        Material newMaterial = SmashingMaterial.getMaterial(originalMaterial);
        if (newMaterial != null) {
            event.setDropItems(false);
            block.setType(Material.AIR);

            ItemStack drop = new ItemStack(newMaterial);
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
            event.setCancelled(true);
        }
    }
}
