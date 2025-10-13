package tools.redstone.redstonetools.mixin.accessors;

import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SubtitlesHud.class)
public interface SubtitlesHudAccessor {
	@Accessor
	List<SubtitlesHud.SubtitleEntry> getEntries();
	@Accessor
	List<SubtitlesHud.SubtitleEntry> getAudibleEntries();
}
