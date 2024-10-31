package hn.blacknight0981.craft.listeners;

import hn.blacknight0981.craft.enchants.ExcavatingEnchant;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ExcavatingListener implements Listener {

    private final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
    private final Enchantment excavating = registry.get(ExcavatingEnchant.KEY);

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (excavating == null) return;
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        if (!tool.containsEnchantment(excavating)) return;

        event.getPlayer().sendMessage("正在使用 挖掘 破壞");

        Block block = event.getBlock();
        World world = block.getWorld();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    // 跳過中心方塊（即玩家實際破壞的方塊）
                    if (x == 0 && y == 0 && z == 0) continue;

                    Block relativeBlock = world.getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

                    if (relativeBlock.getType().isAir() || !relativeBlock.getType().isBlock()) continue;

                    // 如果玩家不是生存 則直接取消模擬
                    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
                        relativeBlock.setType(Material.AIR);
                        continue;
                    }

                    relativeBlock.breakNaturally(tool);
                }
            }
        }
    }
}
