package tools.redstone.redstonetools.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
//~ if paper 'fabric.Fabric' -> 'bukkit.Bukkit'
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class WorldEditUtils {
	/**
	 * Adapts a Minecraft server player to a WorldEdit actor.
	 * This is the only platform-specific part of the WorldEdit integration.
	 */
	public static Player getActor(ServerPlayer player) {
		if (!DependencyLookup.WORLDEDIT_PRESENT) {
			throw new IllegalStateException("WorldEdit is not loaded.");
		}

		//? if <26.1 {
		/*return FabricAdapter.adaptPlayer(player);
		 *///? } else if fabric {
		/*return FabricAdapter.get().fromNativePlayer(player);
		 *///? } else
		return BukkitAdapter.adapt(player.getBukkitEntity());
	}

	public static LocalSession getSession(ServerPlayer player) {
		return WorldEdit.getInstance()
			.getSessionManager()
			.get(getActor(player));
	}

	public static Region getSelection(ServerPlayer player) throws CommandSyntaxException {
		var localSession = getSession(player);

		try {
			return localSession.getSelection(localSession.getSelectionWorld());
		} catch (IncompleteRegionException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Please make a selection with WorldEdit first")).create();
		}
	}
}
