package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.features.commands.OpenScreenFeature;
import tools.redstone.redstonetools.malilib.config.Configs;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Final
	public InGameHud inGameHud;

	@Shadow
	private boolean disconnecting;

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void preventScreenOpening(Screen screen, CallbackInfo ci) {
		if (screen == null && this.disconnecting) {
			ci.cancel(); // prevent boom
		}
		if (Configs.Kr1v.preventClosingOnce) return;
		String currentScreenClass;
		if (screen == null) currentScreenClass = "null";
		else currentScreenClass = screen.getClass().getSimpleName();
		for (String s : Configs.Kr1v.PREVENT_OPENING_OF_SCREEN.getStrings()) {
			if (s.equals(currentScreenClass)) {
				ci.cancel();
				return;
			}
		}
		if (Configs.Kr1v.PREVENT_OPENING_OF_SCREEN_PRINT.getBooleanValue())
			this.inGameHud.getChatHud().addMessage(Text.of("Allowed screen opening of class: " + currentScreenClass));
		if (screen == null) return;
		OpenScreenFeature.INSTANCE.savedScreens.put(currentScreenClass, screen);
	}
}
