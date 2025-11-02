package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;

@Mixin(Vec3d.class)
public class Vec3dMixin {
	@WrapMethod(method = "getX")
	private double getX(Operation<Double> original) {
		if (Configs.Kr1v.RANDOMISE_X.getBooleanValue())
			return original.call() + (Statics.random.nextDouble() - 0.5) * Configs.Kr1v.X_FACTOR.getFloatValue();
		else
			return original.call();
	}
	@WrapMethod(method = "getY")
	private double getY(Operation<Double> original) {
		if (Configs.Kr1v.RANDOMISE_Y.getBooleanValue())
			return original.call() + (Statics.random.nextDouble() - 0.5) * Configs.Kr1v.Y_FACTOR.getFloatValue();
		else
			return original.call();
	}
	@WrapMethod(method = "getZ")
	private double getZ(Operation<Double> original) {
		if (Configs.Kr1v.RANDOMISE_Z.getBooleanValue())
			return original.call() + (Statics.random.nextDouble() - 0.5) * Configs.Kr1v.Z_FACTOR.getFloatValue();
		else
			return original.call();
	}
}
