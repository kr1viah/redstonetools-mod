package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;

import java.util.Random;

@Mixin({
	BufferBuilder.class,
	VertexConsumers.Dual.class,
	OutlineVertexConsumerProvider.OutlineVertexConsumer.class,
	OverlayVertexConsumer.class,
	SpriteTexturedVertexConsumer.class,
	VertexConsumers.Union.class
})
public class VertexConsumerMixin {
	@Unique
	private final Random random = new Random();

	@WrapMethod(method = "vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;")
	private VertexConsumer wrap(float x, float y, float z, Operation<VertexConsumer> original) {
		if (Statics.checkDrawCalls()) {
			return (VertexConsumer) this;
		}
		else {
			Statics.increaseDrawCalls();

			if (Configs.Kr1v.RANDOMISE_X.getBooleanValue()) x += random.nextFloat() * 2 * Configs.Kr1v.X_FACTOR.getFloatValue() - 0.5f;
			if (Configs.Kr1v.RANDOMISE_Y.getBooleanValue()) y += random.nextFloat() * 2 * Configs.Kr1v.Y_FACTOR.getFloatValue() - 0.5f;
			if (Configs.Kr1v.RANDOMISE_Z.getBooleanValue()) z += random.nextFloat() * 2 * Configs.Kr1v.Z_FACTOR.getFloatValue() - 0.5f;

			return original.call(x, y, z);
		}
	}
}
