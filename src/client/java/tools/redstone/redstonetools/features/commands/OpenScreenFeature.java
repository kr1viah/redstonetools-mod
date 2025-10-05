package tools.redstone.redstonetools.features.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class OpenScreenFeature {
	public static final OpenScreenFeature INSTANCE = new OpenScreenFeature();
	public final Map<String, Screen> savedScreens = new HashMap<>();

	protected OpenScreenFeature() {
	}

	public void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(literal("openscreen")
			.then(argument("screen", StringArgumentType.string())
				.suggests((commandContext, suggestionsBuilder) -> {
					List<String> screenNamesList = new ArrayList<>(savedScreens.keySet());
					screenNamesList.sort(String.CASE_INSENSITIVE_ORDER);
					return CommandSource.suggestMatching(screenNamesList, suggestionsBuilder);
				})
				.executes(this::execute)));
	}

	protected int execute(CommandContext<FabricClientCommandSource> context) {
		String screenName = context.getArgument("screen", String.class);
		MinecraftClient.getInstance().send(() -> MinecraftClient.getInstance().setScreen(savedScreens.getOrDefault(screenName, null)));
		return 1;
	}
}
