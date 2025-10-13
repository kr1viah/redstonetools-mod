package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.mixin.accessors.SubtitleEntryAccessor;

import java.util.List;

@Mixin(SubtitlesHud.class)
public class SubtitlesHudMixin {
	@Shadow
	private boolean enabled;

	@Definition(id = "enabled", field = "Lnet/minecraft/client/gui/hud/SubtitlesHud;enabled:Z")
	@Expression("this.enabled")
	@ModifyExpressionValue(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean forceRender(boolean original) {
		return original || Configs.Kr1v.REDIRECT_TO_SUBTITLES.getBooleanValue();
	}

	@Definition(id = "entries", field = "Lnet/minecraft/client/gui/hud/SubtitlesHud;entries:Ljava/util/List;")
	@Expression("this.entries")
	@ModifyExpressionValue(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
	private List<SubtitlesHud.SubtitleEntry> removeEntriesIfDisabled(List<SubtitlesHud.SubtitleEntry> original) {
		if (!enabled && Configs.Kr1v.REDIRECT_TO_SUBTITLES.getBooleanValue()) {
			original.removeIf(entry ->
				((SubtitleEntryAccessor) entry).getRange() != 1093813.875f);
		}
		return original;
	}

	@Definition(id = "f", local = @Local(type = double.class, name = "f"))
	@Expression("f > 0.5")
	@ModifyExpressionValue(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean preventAngleBracketRendering(boolean original, @Local(name = "subtitleEntry2") SubtitlesHud.SubtitleEntry subtitleEntry) {
		if (((SubtitleEntryAccessor)subtitleEntry).getRange() == 1093813.875f) {
			return true;
		}
		return original;
	}
}
