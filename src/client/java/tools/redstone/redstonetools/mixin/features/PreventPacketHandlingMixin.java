package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.BundlePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;

@Mixin(value = {ClientConnection.class})
public class PreventPacketHandlingMixin {
	@Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
	private static <T extends PacketListener> void injected(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
		if (packet.getPacketType().side().getName().equals("serverbound") && !Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_C2S.getBooleanValue()) return;
		if (packet.getPacketType().side().getName().equals("clientbound") && !Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_S2C.getBooleanValue()) return;
		if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_IGNORE.getStrings().contains(packet.getClass().getSimpleName())) return;

		double chance = Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS.getDoubleValue();
		StringBuilder toPrint = new StringBuilder("Prevented packet " + packet.getClass().getSimpleName() + " from getting handled");
		if (packet instanceof BundlePacket<T> thing) {
			toPrint = new StringBuilder("Prevented packets ");
			int size = 0;
			for (Packet<? super T> packet1 : thing.getPackets()) {
				if (size == 0) toPrint.append(", ");
				toPrint.append(packet1.getClass().getSimpleName());
				if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_IGNORE.getStrings().contains(packet1.getClass().getSimpleName())) return;
				size++;
			}
			chance /= size;
			toPrint.append(" from getting handled");
		}
		if (Math.random() < chance) {
			ci.cancel();
			if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_PRINT.getBooleanValue()) {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(toPrint.toString()));
			}
		}
	}
}
