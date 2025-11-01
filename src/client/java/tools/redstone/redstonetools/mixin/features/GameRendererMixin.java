package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void noRenderHand(Camera camera, float tickProgress, Matrix4f positionMatrix, CallbackInfo ci) {
		if (!Configs.Kr1v.DONT_RENDER_HAND.getBooleanValue()) {
			ci.cancel();
		}
	}

	@Definition(id = "drawContext", local = @Local(type = DrawContext.class))
	@Definition(id = "draw", method = "Lnet/minecraft/client/gui/DrawContext;draw()V")
	@Expression("drawContext.draw()")
	@Inject(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
	private void drawText(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
		int temp = Statics.currentDrawCalls;
		Statics.currentDrawCalls = 0;
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Draw calls this frame: " + Statics.lastFrameDrawCalls, 0, 0, 0xFFFFFFFF, true);
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Attempted draw calls this frame: " + Statics.attemptedDrawCalls, 0, 11, 0xFFFFFFFF, true);
		Statics.currentDrawCalls = temp;
	}
}
