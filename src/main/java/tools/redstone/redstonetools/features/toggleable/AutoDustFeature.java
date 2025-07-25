package tools.redstone.redstonetools.features.toggleable;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import static net.minecraft.server.command.CommandManager.literal;

public class AutoDustFeature extends ToggleableFeature {
	public static void registerCommand() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("autodust")
				.executes(context -> new AutoDustFeature().toggle(context))));
	}
}
