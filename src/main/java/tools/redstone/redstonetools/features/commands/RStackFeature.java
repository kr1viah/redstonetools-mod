package tools.redstone.redstonetools.features.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.util.Direction;
import tools.redstone.redstonetools.Commands;
import tools.redstone.redstonetools.features.logic.RStackOperation;
import tools.redstone.redstonetools.utils.ArgumentUtils;
import tools.redstone.redstonetools.utils.DirectionArgument;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import tools.redstone.redstonetools.utils.WorldEditUtils;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static tools.redstone.redstonetools.utils.DirectionUtils.directionToBlock;
import static tools.redstone.redstonetools.utils.DirectionUtils.matchDirection;

public class RStackFeature {
	public static final RStackFeature INSTANCE = new RStackFeature();

	protected RStackFeature() {
	}

	public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, net.minecraft.commands.Commands.CommandSelection registrationEnvironment) {
			dispatcher.register(
				literal("/rstack")
					.requires(Commands.getPerm("rstack"))
					.executes(getCommandForArgumentCount(0))
					.then(argument("count", IntegerArgumentType.integer())
						.executes(getCommandForArgumentCount(1))
						.then(argument("direction", StringArgumentType.string()).suggests(ArgumentUtils.DIRECTION_SUGGESTION_PROVIDER)
							.executes(getCommandForArgumentCount(2))
							.then(argument("offset", IntegerArgumentType.integer())
								.executes(getCommandForArgumentCount(3))
								.then(argument("moveSelection", BoolArgumentType.bool())
									.executes(getCommandForArgumentCount(4)))))));
	}

	protected Command<CommandSourceStack> getCommandForArgumentCount(int argNum) {
		return context -> execute(context, argNum);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int argCount) throws CommandSyntaxException {
		int count = argCount >= 1 ? IntegerArgumentType.getInteger(context, "count") : 1;
		DirectionArgument direction = argCount >= 2 ? ArgumentUtils.parseDirection(context, "direction") : DirectionArgument.ME;
		int offset = argCount >= 3 ? IntegerArgumentType.getInteger(context, "offset") : 2;
		boolean moveSelection = argCount >= 4 && BoolArgumentType.getBool(context, "moveSelection");
		return execute(context, count, offset, direction, moveSelection);
	}

	protected int execute(CommandContext<CommandSourceStack> context, int count, int offset, DirectionArgument direction, boolean moveSelection) throws CommandSyntaxException {
		var player = context.getSource().getPlayerOrException();
		var actor = WorldEditUtils.getActor(player);
		var localSession = WorldEditUtils.getSession(player);
		var selection = WorldEditUtils.getSelection(player);

		Direction stackDirection;
		try {
			stackDirection = matchDirection(direction, actor.getLocation().getDirectionEnum());
		} catch (Exception ex) {
			throw new SimpleCommandExceptionType(Component.literal(ex.getMessage())).create();
		}

		var offsets = RStackOperation.offsets(
			Objects.requireNonNull(directionToBlock(stackDirection)), offset, count);

		try (var editSession = localSession.createEditSession(actor)) {
			RStackOperation.apply(editSession, selection, offsets);

			if (moveSelection && !offsets.isEmpty()) {
				selection.shift(offsets.getLast());
			}

			localSession.remember(editSession);
		} catch (WorldEditException ex) {
			throw new SimpleCommandExceptionType(Component.literal("Stack failed: " + ex.getMessage())).create();
		}

		context.getSource().sendSystemMessage(Component.literal("Stacked %s time(s).".formatted(count)));
		return 1;
	}
}
