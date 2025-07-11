package tools.redstone.redstonetools.features.commands;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import tools.redstone.redstonetools.features.AbstractFeature;
import tools.redstone.redstonetools.features.feedback.AbstractFeedbackSender;
import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.CommandUtils;
import tools.redstone.redstonetools.utils.ReflectionUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

import static tools.redstone.redstonetools.RedstoneToolsClient.INJECTOR;


public abstract class CommandFeature extends AbstractFeature {
    private static final List<KeyBinding> keyBindings = new ArrayList<>();

    @Override
    public void register() {
        super.register();

        var containsRequiredArguments = ReflectionUtils.getArguments(getClass()).stream()
                .anyMatch(a -> !a.isOptional());
        if (containsRequiredArguments) {
            return;
        }

        var info = ReflectionUtils.getFeatureInfo(getClass());
        var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                info.name(),
                InputUtil.Type.KEYSYM,
                -1,
                "Redstone Tools"
        ));

        keyBindings.add(keyBinding);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                assert client.player != null;
                client.player.networkHandler.sendChatCommand("/" + info.command());
            }
        });
    }

    @Override
    protected void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        var info = ReflectionUtils.getFeatureInfo(getClass());
        var arguments = ReflectionUtils.getArguments(getClass());

        CommandUtils.register(
                info.command(),
                arguments,
                context -> {
                    for (var argument : arguments) {
                        argument.updateValue(context);
                    }

                    var feedback = execute(context.getSource());

                    INJECTOR.getInstance(AbstractFeedbackSender.class)
                            .sendFeedback(context.getSource(), feedback);

                    return feedback.getType().getCode();
                },
                dispatcher,
                dedicated);
    }

    protected abstract Feedback execute(ServerCommandSource source) throws CommandSyntaxException;
}
