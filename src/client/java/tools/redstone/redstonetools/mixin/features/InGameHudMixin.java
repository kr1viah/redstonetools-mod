package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.screen.BetterChatHud;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Definition(id = "ChatHud", type = ChatHud.class)
	@Definition(id = "client", local = @Local(type = MinecraftClient.class, argsOnly = true))
	@Expression("new ChatHud(client)")
	@ModifyExpressionValue(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
	private ChatHud injected(ChatHud original) {
		if (Configs.Kr1v.CHAT_SELECTING.getBooleanValue())
			return new BetterChatHud(MinecraftClient.getInstance());
		return original;
	}
}
