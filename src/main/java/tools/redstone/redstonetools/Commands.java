package tools.redstone.redstonetools;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//? if >=1.21.11 {
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
//? }
import tools.redstone.redstonetools.features.commands.*;
import tools.redstone.redstonetools.features.toggleable.*;
import tools.redstone.redstonetools.utils.DependencyLookup;

//? if fabric {
import net.minecraft.commands.CommandSourceStack;
//? } else {
/*import io.papermc.paper.command.brigadier.CommandSourceStack;
*///? }

import java.util.function.Predicate;

public class Commands {
	//? if fabric {
	public static final Predicate<CommandSourceStack> PERMISSION_LEVEL_2 =
		//? if <=1.21.10 {
		/*source -> source.hasPermission(2);
		*///?} else {
		net.minecraft.commands.Commands.hasPermission(new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER));
		//?}
	//? } else {
	/*public static final Predicate<CommandSourceStack> PERMISSION_LEVEL_2 = sender -> sender.getSender().hasPermission("permission.test");
	*///? }

	public static void registerCommands(/*? paper {*//*io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> events*//*? }*/) {
		//? if fabric {
		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
		 //? } else {
		/*events.registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, event -> {
			var commandDispatcher = event.registrar().getDispatcher();
		*///? }
			if (DependencyLookup.WORLDEDIT_PRESENT) {
				BinaryBlockReadFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				ColorCodeFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				MinSelectionFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
				RStackFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			}
			ReachFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			BaseConvertFeature.INSTANCE.registerCommand(commandDispatcher);
			GiveMeFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess);
			ItemComponentsFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ItemBindFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			QuickTpFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			SignalStrengthBlockFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			AutoDustFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			AutoRotateFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ClickContainerFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			ColoredFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
			CopyStateFeature.INSTANCE.registerCommand(commandDispatcher, commandRegistryAccess, registrationEnvironment);
		});
	}
}
