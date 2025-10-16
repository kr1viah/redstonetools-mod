package tools.redstone.redstonetools.malilib.gui.button;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IConfigGui;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import tools.redstone.redstonetools.malilib.config.IConfigStringMap;
import tools.redstone.redstonetools.malilib.gui.GuiStringMapEdit;

public class ConfigButtonStringMap extends ButtonGeneric {
    private final IConfigStringMap config;
    private final IConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonStringMap(int x, int y, int width, int height, IConfigStringMap config, IConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
        super(x, y, width, height, "");

        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;

        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClickedImpl(mouseX, mouseY, mouseButton);

        if (this.dialogHandler != null) {
            this.dialogHandler.openDialog(new GuiStringMapEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiStringMapEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        boolean addDivider = false;
        for (Pair<String, String> entry : this.config.getMap()) {
			String key = entry.getLeft();
            String value = entry.getRight();
            if (addDivider) {
                sb.append(", ");
            }
            sb.append(key).append("=").append(value);
            addDivider = true;
        }
        sb.append("}");

        this.displayString = sb.toString();
    }
}
