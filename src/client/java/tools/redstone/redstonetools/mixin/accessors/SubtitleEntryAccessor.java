package tools.redstone.redstonetools.mixin.accessors;

import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SubtitlesHud.SubtitleEntry.class)
public interface SubtitleEntryAccessor {
	@Accessor float getRange();
}
