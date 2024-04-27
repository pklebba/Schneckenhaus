package io.github.lama06.schneckenhaus.snell.shulker;

import io.github.lama06.schneckenhaus.command.InfoCommand;
import io.github.lama06.schneckenhaus.position.GridPosition;
import io.github.lama06.schneckenhaus.snell.Shell;
import io.github.lama06.schneckenhaus.util.MaterialUtil;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ShulkerShell extends Shell {
    private final ShulkerShellConfig config;

    public ShulkerShell(final GridPosition position, final ShulkerShellConfig config) {
        super(position, config);
        this.config = config;
    }

    @Override
    public Map<Block, BlockData> getBlocks() {
        final Map<Block, BlockData> blocks = new HashMap<>();
        addFloorBlocks(blocks);
        addWallBlocks(blocks);
        addDoorBlocks(blocks);
        addRoofBlocks(blocks);
        return blocks;
    }

    @Override
    public List<InfoCommand.Entry> getInformation() {
        final List<InfoCommand.Entry> information = new ArrayList<>(super.getInformation());
        information.add(new InfoCommand.Entry("Color", getColor().toString().toLowerCase(), config.getItemColor()));
        return information;
    }

    private void addFloorBlocks(final Map<Block, BlockData> blocks) {
        final BlockData terracotta = MaterialUtil.getColoredTerracotta(getColor()).createBlockData();
        final Block cornerBlock = getPosition().getCornerBlock();
        for (int x = cornerBlock.getX(); x <= cornerBlock.getX() + getSize() + 1; x++) {
            for (int z = cornerBlock.getZ(); z <= cornerBlock.getZ() + getSize() + 1; z++) {
                blocks.put(getWorld().getBukkit().getBlockAt(x, cornerBlock.getY(), z), terracotta);
            }
        }
    }

    private void addWallBlocks(final Map<Block, BlockData> blocks, final BlockFace side) {
        final BlockData terracotta = MaterialUtil.getColoredTerracotta(getColor()).createBlockData();
        final BlockData concrete = MaterialUtil.getColoredConcrete(getColor()).createBlockData();
        final Block cornerBlock = getPosition().getCornerBlock();
        for (int y = cornerBlock.getY() + 1; y <= cornerBlock.getY() + getSize(); y++) {
            final int horizontalStart;
            if (side == BlockFace.EAST || side == BlockFace.WEST) {
                horizontalStart = cornerBlock.getZ();
            } else {
                horizontalStart = cornerBlock.getX();
            }
            final int horizontalEnd = horizontalStart + getSize() + 1;
            for (int horizontalCoordinate = horizontalStart; horizontalCoordinate <= horizontalEnd; horizontalCoordinate++) {
                final Block block;
                if (side == BlockFace.EAST || side == BlockFace.WEST) {
                    block = getWorld().getBukkit().getBlockAt(
                            side == BlockFace.EAST ? cornerBlock.getX() : cornerBlock.getX() + getSize() + 1,
                            y,
                            horizontalCoordinate
                    );
                } else {
                    block = getWorld().getBukkit().getBlockAt(
                            horizontalCoordinate,
                            y,
                            side == BlockFace.SOUTH ? cornerBlock.getZ() : cornerBlock.getZ() + getSize() + 1
                    );
                }
                if (block.equals(getPosition().getLowerDoorBlock()) || block.equals(getPosition().getUpperDoorBlock())) {
                    continue;
                }
                final BlockData blockData = y <= cornerBlock.getY() + 1 ? terracotta : concrete;
                blocks.put(block, blockData);
            }
        }
    }

    private void addWallBlocks(final Map<Block, BlockData> blocks) {
        for (final BlockFace side : List.of(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST)) {
            addWallBlocks(blocks, side);
        }
    }

    private void addRoofBlocks(final Map<Block, BlockData> blocks) {
        final BlockData glass = MaterialUtil.getColoredGlass(getColor()).createBlockData();
        final Block cornerBlock = getPosition().getCornerBlock();
        final int y = cornerBlock.getY() + getSize() + 1;
        for (int x = cornerBlock.getX(); x <= cornerBlock.getX() + getSize() + 1; x++) {
            for (int z = cornerBlock.getZ(); z <= cornerBlock.getZ() + getSize() + 1; z++) {
                blocks.put(getWorld().getBukkit().getBlockAt(x, y, z), glass);
            }
        }
    }

    private DyeColor getColor() {
        return config.getColor();
    }
}
