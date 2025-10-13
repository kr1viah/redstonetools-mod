package tools.redstone.redstonetools.mixin.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.malilib.config.MacroManager;
import tools.redstone.redstonetools.mixin.accessors.InGameHudAccessor;
import tools.redstone.redstonetools.mixin.accessors.SubtitlesHudAccessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Mixin(ChatHud.class)
public class ChatHudMixin {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), cancellable = true)
	private void injected(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
		if (MacroManager.shouldMute) ci.cancel();
		for (String str : Configs.Kr1v.CHAT_HIDE.getStrings()) {
			try {
				Pattern pattern = Pattern.compile(str);
				Matcher matcher = pattern.matcher(message.getString());
				if (matcher.matches()) {
					ci.cancel();
					if (Configs.Kr1v.REDIRECT_TO_SUBTITLES.getBooleanValue()) {
						if (MinecraftClient.getInstance().player != null) {
							List<SubtitlesHud.SubtitleEntry> entries = ((SubtitlesHudAccessor)((InGameHudAccessor)MinecraftClient.getInstance().inGameHud).getSubtitlesHud()).getEntries();
							if (!Configs.Kr1v.ALLOW_DUPLICATE_SUBTITLES.getBooleanValue()){
								List<SubtitlesHud.SubtitleEntry> audibleEntries = ((SubtitlesHudAccessor) ((InGameHudAccessor) MinecraftClient.getInstance().inGameHud).getSubtitlesHud()).getAudibleEntries();
								for (var entry : audibleEntries) {
									if (entry.getText().copy().equals(message.copy())) {
										audibleEntries.remove(entry);
										entries.remove(entry);
										break;
									}
								}
							}
							entries.add(new SubtitlesHud.SubtitleEntry(message, 1093813.875f, MinecraftClient.getInstance().player.getPos()));
						}
					}
					return;
				}
			} catch (PatternSyntaxException ignored) {
			}
		}
	}
}
