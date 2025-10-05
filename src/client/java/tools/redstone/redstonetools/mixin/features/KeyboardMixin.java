package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(
		method = "onKey",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(III)Z"
		)
	)
	private void forceCloseScreen(long window, int keyCode, int scancode, int action, int modifiers, CallbackInfo ci) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE && Configs.Kr1v.ALWAYS_CLOSE_ON_ESC.getBooleanValue() && action == 1) {
			Configs.Kr1v.preventClosingOnce = true;
			if (client.currentScreen != null)
				client.currentScreen.close();
			Configs.Kr1v.preventClosingOnce = false;
		}
	}
}
