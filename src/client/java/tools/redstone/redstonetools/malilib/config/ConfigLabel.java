package tools.redstone.redstonetools.malilib.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigBase;
import org.jetbrains.annotations.Nullable;

public class ConfigLabel implements IConfigBase {
	String comment;
	public ConfigLabel(String comment) {
		this.comment = comment;
	}

	@Override
	public ConfigType getType() {
		return null;
	}

	@Override
	public String getName() {
		return comment;
	}

	@Override
	public @Nullable String getComment() {
		return comment;
	}

	@Override
	public String getTranslatedName() {
		return comment;
	}

	@Override
	public void setPrettyName(String s) {}

	@Override
	public void setTranslatedName(String s) {}

	@Override
	public void setComment(String s) {
		comment = s;
	}

	@Override
	public void setValueFromJsonElement(JsonElement jsonElement) {}

	@Override
	public JsonElement getAsJsonElement() {return null;}
}
