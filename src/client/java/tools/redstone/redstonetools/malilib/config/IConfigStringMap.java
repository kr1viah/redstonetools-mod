package tools.redstone.redstonetools.malilib.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import net.minecraft.util.Pair;

import java.util.List;

public interface IConfigStringMap extends IConfigBase {
    List<Pair<String, String>> getMap();

    ImmutableList<Pair<String, String>> getDefaultMap();

    void setMap(List<Pair<String, String>> newMap);

    void setModified();

}
