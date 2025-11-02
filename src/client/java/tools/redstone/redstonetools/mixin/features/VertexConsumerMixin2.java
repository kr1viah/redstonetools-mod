package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.SpriteTexturedVertexConsumer;
import net.minecraft.client.render.VertexConsumers;
import org.spongepowered.asm.mixin.Mixin;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;

@Mixin({
	BufferBuilder.class,
	VertexConsumers.Dual.class,
	SpriteTexturedVertexConsumer.class,
	VertexConsumers.Union.class
})
public class VertexConsumerMixin2 {
	@WrapMethod(method = "vertex(FFFIFFIIFFF)V")
	private void injected(float x, float y, float z, int color, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ, Operation<Void> original) {
		if (Statics.shouldAffect() && Statics.shouldRender()) {
			Statics.increaseDrawCalls();

			if (Configs.Kr1v.RANDOMISE_X.getBooleanValue()) x += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.X_FACTOR.getFloatValue();
			if (Configs.Kr1v.RANDOMISE_Y.getBooleanValue()) y += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.Y_FACTOR.getFloatValue();
			if (Configs.Kr1v.RANDOMISE_Z.getBooleanValue()) z += (Statics.random.nextFloat() - 0.5f) * 2 * Configs.Kr1v.Z_FACTOR.getFloatValue();

			original.call(x, y, z, color, u, v, overlay, light, normalX, normalY, normalZ);
		} else if (Statics.shouldRender()) {
			original.call(x, y, z, color, u, v, overlay, light, normalX, normalY, normalZ);
		}
	}
}
