package tools.redstone.redstonetools.mixin.features.flight;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.malilib.config.Configs;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

	// if (!playerAbilities.flying) {
	@Definition(id = "playerAbilities", local = @Local(type = PlayerAbilities.class))
	@Definition(id = "flying", field = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z")
	@Expression("playerAbilities.flying")
	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
	private boolean injected2(boolean original) {
		if (Configs.Kr1v.PREVENT_FLIGHT_STATE_CHANGE.getBooleanValue()) {
			return false;
		} else {
			return original;
		}
	}

	// playerAbilities.flying = !playerAbilities.flying;
	@Definition(id = "playerAbilities", local = @Local(type = PlayerAbilities.class))
	@Definition(id = "flying", field = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z")
	@Expression("playerAbilities.flying")
	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
	private boolean injected3(boolean original) {
		if (Configs.Kr1v.PREVENT_FLIGHT_STATE_CHANGE.getBooleanValue()) {
			return !original;
		} else {
			return original;
		}
	}

	// if (this.isOnGround() && playerAbilities.flying && !this.client.interactionManager.isFlyingLocked()) {
	@ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnGround()Z", ordinal = 1))
	private boolean injected(boolean original) {
		if (Configs.Kr1v.PREVENT_FLIGHT_STATE_CHANGE.getBooleanValue()) {
			return false;
		}
		return original;
	}
}
