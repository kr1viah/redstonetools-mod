package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void noRenderHand(Camera camera, float tickProgress, Matrix4f positionMatrix, CallbackInfo ci) {
		if (!Configs.Kr1v.DONT_RENDER_HAND.getBooleanValue()) {
			ci.cancel();
		}
	}
}
