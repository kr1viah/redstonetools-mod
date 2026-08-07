package tools.redstone.redstonetools.features.logic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * The geometry and the copy loop behind //rstack. Knows WorldEdit, knows nothing about Brigadier
 * or about how the arguments were spelled.
 */
public final class RStackOperation {
	private RStackOperation() {
	}

	/**
	 * @param direction unit vector the copies travel along
	 * @param spacing   blocks between two consecutive copies
	 *
	 * @return Offset of every copy, first to last.
	 */
	public static List<BlockVector3> offsets(BlockVector3 direction, int spacing, int count) {
		List<BlockVector3> offsets = new ArrayList<>(Math.max(0, count));
		BlockVector3 step = direction.multiply(spacing);

		for (int i = 1; i <= count; i++) {
			offsets.add(step.multiply(i));
		}

		return offsets;
	}

	/** Copies the selection once per offset. Air in the source is left untouched. */
	public static void apply(EditSession editSession, Region selection, List<BlockVector3> offsets) throws WorldEditException {
		ExistingBlockMask sourceMask = new ExistingBlockMask(selection.getWorld());

		for (BlockVector3 offset : offsets) {
			ForwardExtentCopy copy = new ForwardExtentCopy(
				editSession,
				selection,
				editSession,
				selection.getMinimumPoint().add(offset));

			copy.setSourceMask(sourceMask);
			copy.setSourceFunction(position -> false);
			Operations.complete(copy);
		}
	}
}