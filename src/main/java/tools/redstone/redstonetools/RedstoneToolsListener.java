package tools.redstone.redstonetools;
//? paper {
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.impl.CraftRedStoneWire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.utils.ColoredBlock;

public class RedstoneToolsListener implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (AutoDustFeature.INSTANCE.isEnabled(event.getPlayer().getUniqueId())) {
			var dustPos = event.getBlockPlaced().getLocation().add(0, 1, 0);
			var block = event.getBlockPlaced();
			var blockAbove = event.getBlockPlaced().getWorld().getBlockAt(dustPos);

			// todo: breakpoint here to see how to get the blocks identifier
			if (blockAbove != Blocks.AIR || ColoredBlock.fromBlockId(block.toString()) == null) {
				return;
			}

			event.getPlayer().getWorld().setBlockData(dustPos, CraftRedStoneWire.createData(Blocks.REDSTONE_WIRE.defaultBlockState()));
		}

		// todo: test
		if (AutoRotateFeature.INSTANCE.isEnabled(event.getPlayer().getUniqueId()))  {
			Block block = event.getBlockPlaced();
			BlockData data = block.getBlockData();

			BlockData rotated = data.clone();
			rotated.rotate(org.bukkit.block.structure.StructureRotation.CLOCKWISE_180);

			if (!rotated.matches(data)) {
				block.setBlockData(rotated, false);
			}
		}
	}
}
//? }