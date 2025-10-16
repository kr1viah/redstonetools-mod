package tools.redstone.redstonetools.mixin.malilib;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigResettable;
import fi.dy.masa.malilib.config.IConfigStringList;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tools.redstone.redstonetools.malilib.config.IConfigStringMap;
import tools.redstone.redstonetools.malilib.config.options.ConfigStringMap;
import tools.redstone.redstonetools.malilib.gui.button.ConfigButtonStringMap;

// magic!
@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class WidgetConfigOptionMixin {
    @Unique
    protected ImmutableList<Pair<String, String>> initialStringMap;

    @Shadow
    @Final
    protected IKeybindConfigGui host;

    @Shadow
    protected abstract void addConfigButtonEntry(int xReset, int yReset, IConfigResettable config, ButtonBase optionButton);

    @Definition(id = "config", local = @Local(type = IConfigBase.class))
    @Definition(id = "IConfigStringList", type = IConfigStringList.class)
    @Expression("config instanceof IConfigStringList")
    @Inject(method = "<init>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private void initStringMap(int x, int y, int width, int height, int labelWidth, int configWidth, GuiConfigsBase.ConfigOptionWrapper wrapper, int listIndex, IKeybindConfigGui host, WidgetListConfigOptionsBase<?, ?> parent, CallbackInfo ci, @Local IConfigBase config) {
        if (config instanceof IConfigStringMap) {
            this.initialStringMap = ImmutableList.copyOf(((IConfigStringMap) config).getMap());
        }
    }

    @Definition(id = "config", local = @Local(type = IConfigBase.class, argsOnly = true))
    @Definition(id = "ConfigBooleanHotkeyed", type = ConfigBooleanHotkeyed.class)
    @Expression("config instanceof ConfigBooleanHotkeyed")
    @Inject(method = "addConfigOption", at = @At("MIXINEXTRAS:EXPRESSION"), cancellable = true)
    private void addConfigOptionStringMap(int x, int y, float zLevel, int labelWidth, int configWidth, IConfigBase config, CallbackInfo ci, @Local(name = "configHeight") int configHeight) {
        if (config instanceof ConfigStringMap) {
            ConfigButtonStringMap optionButton = new ConfigButtonStringMap(x, y, configWidth, configHeight, (IConfigStringMap) config, this.host, this.host.getDialogHandler());
            this.addConfigButtonEntry(x + configWidth + 2, y, (IConfigResettable) config, optionButton);
            ci.cancel();
        }
    }

    @Definition(id = "config", local = @Local(type = IConfigBase.class))
    @Definition(id = "IConfigStringList", type = IConfigStringList.class)
    @Expression("config instanceof IConfigStringList")
    @Inject(method = "wasConfigModified", at = @At("MIXINEXTRAS:EXPRESSION"), cancellable = true)
    private void wasConfigModifiedStringMap(CallbackInfoReturnable<Boolean> cir, @Local IConfigBase config) {
        if (this.initialStringMap != null && config instanceof IConfigStringMap) {
            cir.setReturnValue(!this.initialStringMap.equals(((IConfigStringMap) config).getMap()));
        }
    }
}
