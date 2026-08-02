package tools.redstone.redstonetools;
//? paper {
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.impl.CraftRedStoneWire;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import tools.redstone.redstonetools.features.toggleable.AutoDustFeature;
import tools.redstone.redstonetools.features.toggleable.AutoRotateFeature;
import tools.redstone.redstonetools.utils.ColoredBlock;
import tools.redstone.redstonetools.features.toggleable.ClickContainerFeature;

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

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
			return;
		}

		ServerPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
		InteractionHand hand = event.getHand() == EquipmentSlot.OFF_HAND
			? InteractionHand.OFF_HAND
			: InteractionHand.MAIN_HAND;

		Block clicked = event.getClickedBlock();
		BlockPos pos = new BlockPos(clicked.getX(), clicked.getY(), clicked.getZ());

		if (ClickContainerFeature.handleUse(player, player.level(), hand, pos)) {
			event.setCancelled(true);
		}
	}
}
//? }