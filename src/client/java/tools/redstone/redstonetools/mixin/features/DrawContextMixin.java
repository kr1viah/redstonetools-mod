package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.ClientCommands;
import tools.redstone.redstonetools.Statics;

@Mixin(DrawContext.class)
public class DrawContextMixin {
	@Inject(
		method = {
			"fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V",
			"fillGradient(Lnet/minecraft/client/render/VertexConsumer;IIIIIII)V",
			"fillWithLayer",
			"drawTexturedQuad",
			"drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
			"drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
			"drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
			"drawItemBar",
			"drawStackCount"

		},
		at = @At("HEAD"),
		cancellable = true)
	public void canceling(CallbackInfo ci) {
		if (Statics.currentDrawCalls > ClientCommands.Configs.Kr1v.MAX_DRAW_CALLS.getIntegerValue()) {
			ci.cancel();
		} else {
			Statics.increaseDrawCalls();
		}
	}

	@Inject(
		method = {
			"drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I",
			"drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"
		},
		at = @At("HEAD"),
		cancellable = true)
	public void canceling2(CallbackInfoReturnable<Integer> cir) {
		if (Statics.currentDrawCalls > ClientCommands.Configs.Kr1v.MAX_DRAW_CALLS.getIntegerValue()) {
			cir.setReturnValue(0);
		} else {
			Statics.increaseDrawCalls();
		}
	}
}
