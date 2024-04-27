package io.github.lama06.schneckenhaus.systems;

import io.github.lama06.schneckenhaus.snell.Shell;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public final class PlaceShellListener implements Listener {
    @EventHandler
    private void preserveShellIdWhenPlaced(final BlockPlaceEvent event) {
        final ItemStack itemInHand = event.getItemInHand();
        final PersistentDataContainer itemData = itemInHand.getItemMeta().getPersistentDataContainer();
        final Integer id = Shell.ITEM_ID.get(itemData);
        if (id == null) {
            return;
        }
        final Block block = event.getBlock();
        if (!(block.getState() instanceof final TileState tileState)) {
            return;
        }
        Shell.BLOCK_ID.set(tileState, id);
        tileState.update();
    }

}
