package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.BundlePacket;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.utils.MappingUtils;

@Mixin(value = {ClientConnection.class})
public class PreventPacketHandlingMixin {
	@WrapMethod(method = "sendInternal")
	private void injected(Packet<?> packet, PacketCallbacks callbacks, boolean flush, Operation<Void> original) {
		String yarnClassName = MappingUtils.intermediaryToYarn(packet.getClass().getSimpleName());
		if (packet.getPacketType().side().getName().equals("clientbound") ||
			(packet.getPacketType().side().getName().equals("serverbound") && !Configs.Kr1v.AFFECT_PACKETS_C2S.getBooleanValue()) ||
			Configs.Kr1v.PACKETS_IGNORE.getStrings().contains(yarnClassName)) {
			original.call(packet, callbacks, flush);
			return;
		}
		if (Math.random() < Configs.Kr1v.PERCENTAGE_DELAYED_PACKETS.getDoubleValue()) {
			if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_PRINT.getBooleanValue()) {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Lagged packet " + yarnClassName));
			}
			new Thread(() -> {
				try {
					Thread.sleep((long) (Math.random() * Configs.Kr1v.PERCENTAGE_DELAYED_PACKETS_TIME.getDoubleValue()));
				} catch (InterruptedException ignored) {
				}

				MinecraftClient.getInstance().send(() -> {
					try {
						original.call(packet, callbacks, flush);
					} catch (Exception ignored) {
					}
				});
			}).start();
			return;
		}
		original.call(packet, callbacks, flush);
	}

	@WrapMethod(method = "handlePacket")
	private static <T extends PacketListener> void injected(Packet<T> packet, PacketListener listener, Operation<Void> original) {
		String yarnClassName = MappingUtils.intermediaryToYarn(packet.getClass().getSimpleName());
		if (packet.getPacketType().side().getName().equals("serverbound") ||
			(packet.getPacketType().side().getName().equals("clientbound") && !Configs.Kr1v.AFFECT_PACKETS_S2C.getBooleanValue()) ||
			Configs.Kr1v.PACKETS_IGNORE.getStrings().contains(yarnClassName)) {
			original.call(packet, listener);
			return;
		}

		double chance = Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS.getDoubleValue();
		StringBuilder toPrint = new StringBuilder("Prevented packet " + yarnClassName + " from getting handled");
		if (packet instanceof BundlePacket<T> thing) {
			toPrint = new StringBuilder("Prevented packets ");
			int size = 0;
			for (Packet<? super T> packet1 : thing.getPackets()) {
				if (size == 0) toPrint.append(", ");
				String packet1YarnName = MappingUtils.intermediaryToYarn(packet1.getClass().getSimpleName());
				toPrint.append(packet1YarnName);
				if (Configs.Kr1v.PACKETS_IGNORE.getStrings().contains(packet1YarnName)) {
					original.call(packet, listener);
					return;
				}
				size++;
			}
			chance /= size;
			toPrint.append(" from getting handled");
		}
		if (Math.random() < chance) {
			if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_PRINT.getBooleanValue()) {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(toPrint.toString()));
			}
			return;
		}
		if (Math.random() < Configs.Kr1v.PERCENTAGE_DELAYED_PACKETS.getDoubleValue()) {
			if (Configs.Kr1v.PERCENTAGE_DROPPED_PACKETS_PRINT.getBooleanValue()) {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("Lagged packet " + yarnClassName));
			}
			new Thread(() -> {
				try {
					Thread.sleep((long) (Math.random() * Configs.Kr1v.PERCENTAGE_DELAYED_PACKETS_TIME.getDoubleValue()));
				} catch (InterruptedException ignored) {
				}

				MinecraftClient.getInstance().send(() -> {
					try {
						original.call(packet, listener);
					} catch (Exception ignored) {
					}
				});
			}).start();
			return;
		}
		original.call(packet, listener);
	}
}
