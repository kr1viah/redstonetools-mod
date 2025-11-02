package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;

@Mixin({
	BufferBuilder.class,
	VertexConsumers.Dual.class,
	OutlineVertexConsumerProvider.OutlineVertexConsumer.class,
	OverlayVertexConsumer.class,
	SpriteTexturedVertexConsumer.class,
	VertexConsumers.Union.class
})
public class VertexConsumerMixin {
	@WrapMethod(method = "vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;")
	private VertexConsumer wrap(float x, float y, float z, Operation<VertexConsumer> original) {
		if (Statics.shouldAffect() && Statics.shouldRender()) {
			Statics.increaseDrawCalls();

			if (Configs.Kr1v.RANDOMISE_X.getBooleanValue())
				x += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.X_FACTOR.getFloatValue();
			if (Configs.Kr1v.RANDOMISE_Y.getBooleanValue())
				y += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.Y_FACTOR.getFloatValue();
			if (Configs.Kr1v.RANDOMISE_Z.getBooleanValue())
				z += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.Z_FACTOR.getFloatValue();

			return original.call(x, y, z);
		} else if (Statics.shouldRender()) {
			return original.call(x, y, z);
		} else {
			return (VertexConsumer) this;
		}
	}
}
