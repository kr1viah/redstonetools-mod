package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.screen.BetterChatHud;

@Mixin(ParentElement.class)
public interface ParentElementMixin {
	@Inject(method = "mouseReleased", at = @At("HEAD"))
	private void injected(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (this instanceof ChatScreen) {
			if (MinecraftClient.getInstance().inGameHud.getChatHud() instanceof BetterChatHud betterChatHud) {
				betterChatHud.mouseReleased(mouseX, mouseY);
			}
		}
	}
}
