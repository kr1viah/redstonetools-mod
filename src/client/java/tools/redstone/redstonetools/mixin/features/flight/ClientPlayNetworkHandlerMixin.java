package tools.redstone.redstonetools.mixin.features.flight;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.Configs;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@ModifyExpressionValue(method = "onPlayerAbilities", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/PlayerAbilitiesS2CPacket;isFlying()Z"))
	private boolean injected(boolean original, @Local PlayerEntity player) {
		if (Configs.Kr1v.PREVENT_FLIGHT_STATE_CHANGE.getBooleanValue()) {
			return player.getAbilities().flying;
		} else {
			return original;
		}
	}
}
