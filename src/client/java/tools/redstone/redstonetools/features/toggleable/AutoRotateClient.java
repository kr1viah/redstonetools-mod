package tools.redstone.redstonetools.features.toggleable;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.packets.SetFeatureEnabledPayload;

public class AutoRotateClient {
	public static ConfigBoolean isEnabled = Configs.Toggles.AUTOROTATE;

	public static void registerHandler() {
	}
}
