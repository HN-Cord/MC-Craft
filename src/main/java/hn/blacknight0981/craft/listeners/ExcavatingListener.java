package hn.blacknight0981.craft.listeners;

import hn.blacknight0981.craft.enchants.ExcavatingEnchant;
import hn.blacknight0981.craft.model.AreaCalculator;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ExcavatingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment excavating = registry.get(ExcavatingEnchant.KEY);

    private final Set<UUID> currentlyBreaking = new HashSet<>(); // 標記

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (excavating == null) return;
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(excavating)) return;
        if (currentlyBreaking.contains(uuid)) return;

        currentlyBreaking.add(uuid);

        try {
            Block block = event.getBlock();
            BlockFace blockFace = player.getTargetBlockFace(10);
            Material blockMaterial = block.getType();

            int excavatingLevel = tool.getEnchantmentLevel(excavating);
            AreaCalculator areaCalculator = new AreaCalculator(excavatingLevel);

            if (blockFace == null) return;
            int[][] offsets = areaCalculator.getOffsets(blockFace);

            for (int[] offset : offsets) {
                Block targetBlock = block.getRelative(offset[0], offset[1], offset[2]);

                if (
                        targetBlock.getType().getHardness() < 0 || // 防止負值
                        targetBlock.getType().getHardness() > blockMaterial.getHardness() + 2) {
                    continue;
                }

                // 創造禁止掉落
                if (player.getGameMode() == GameMode.CREATIVE) {
                    targetBlock.setType(Material.AIR);
                    continue;
                }

                // 模擬自訂義事件
                BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
                Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);

                if (!blockBreakEvent.isCancelled()) {
                    targetBlock.breakNaturally(tool);
                }
            }
        } finally {
            currentlyBreaking.remove(uuid);
        }
    }
}
