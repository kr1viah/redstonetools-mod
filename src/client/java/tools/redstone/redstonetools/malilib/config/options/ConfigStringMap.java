package tools.redstone.redstonetools.malilib.config.options;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.options.ConfigBase;
import net.minecraft.util.Pair;
import tools.redstone.redstonetools.malilib.config.IConfigStringMap;

import java.util.ArrayList;
import java.util.List;

public class ConfigStringMap extends ConfigBase<ConfigStringMap> implements IConfigStringMap {
	private final ImmutableList<Pair<String, String>> defaultMap;
    private final List<Pair<String, String>> map = new ArrayList<>();

    public ConfigStringMap(String name, ImmutableList<Pair<String, String>> defaultValue, String comment, String prettyName, String translatedName) {
        super(null, name, comment, prettyName, translatedName);

        this.defaultMap = defaultValue;
        this.map.addAll(defaultValue);
    }


	@Override
    public List<Pair<String, String>> getMap() {
        return map;
    }

    @Override
    public ImmutableList<Pair<String, String>> getDefaultMap() {
        return defaultMap;
    }

    @Override
    public void setMap(List<Pair<String, String>> newMap) {
        if (!this.map.equals(newMap)) {
            this.map.clear();
            this.map.addAll(newMap);
            this.onValueChanged();
        }
    }

    @Override
    public void setModified() {
        this.onValueChanged();
    }

    @Override
    public void resetToDefault() {
        setMap(defaultMap);
    }

    @Override
    public boolean isModified() {
        return !map.equals(defaultMap);
    }

    private void addEntry(String key, String value) {
        map.add(new Pair<>(key, value));
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        List<Pair<String, String>> oldMap = new ArrayList<>(map);
        map.clear();
        try {
            JsonArray arr = element.getAsJsonArray();
            JsonArray keys = arr.get(0).getAsJsonArray();
            JsonArray values = arr.get(1).getAsJsonArray();
            if (keys.size() == values.size()) {
                final int count = keys.size();
                for (int i = 0; i < count; i++) {
                    this.addEntry(keys.get(i).getAsString(), values.get(i).getAsString());
                }

                if (!oldMap.equals(map)) {
                    onValueChanged();
                }
            }
        } catch (Exception e) {
            MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonArray mapArr = new JsonArray();
        JsonArray keys = new JsonArray();
        JsonArray values = new JsonArray();

        for (Pair<String, String> entry : map) {
            keys.add(entry.getLeft());
            values.add(entry.getRight());
        }
        mapArr.add(keys);
        mapArr.add(values);

        return mapArr;
    }
}
