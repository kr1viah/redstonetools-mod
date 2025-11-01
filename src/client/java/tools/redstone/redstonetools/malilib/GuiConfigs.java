package tools.redstone.redstonetools.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.RedstoneTools;
import tools.redstone.redstonetools.malilib.config.ConfigLabel;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class GuiConfigs extends GuiConfigsBase {
	public static ConfigGuiTab tab = ConfigGuiTab.GENERAL;

	public GuiConfigs() {
		super(10, 50, RedstoneTools.MOD_ID, null, "Configs", "0.0.0");
	}

	@Override
	public void initGui() {
		if (GuiConfigs.tab == ConfigGuiTab.MACROS) {
			GuiConfigsBase.openGui(new GuiMacroManager());
			return;
		}
		super.initGui();
		this.clearOptions();

		int x = 10;
		int y = 26;

		for (ConfigGuiTab tab : ConfigGuiTab.values()) {
			x += this.createButton(x, y, -1, tab);
		}
	}

	private int createButton(int x, int y, int width, ConfigGuiTab tab) {
		ButtonGeneric button = new ButtonGeneric(x, y, width, 20, tab.getDisplayName());
		button.setEnabled(GuiConfigs.tab != tab);
		this.addButton(button, new ButtonListener(tab, this));

		return button.getWidth() + 2;
	}

	@Override
	protected int getConfigWidth() {
		return this.width / 2;
	}

	@Override
	protected boolean useKeybindSearch() {
		return false;
	}

	@Override
	public List<ConfigOptionWrapper> getConfigs() {
		List<? extends IConfigBase> configs;
		ConfigGuiTab tab = GuiConfigs.tab;

		if (tab == ConfigGuiTab.GENERAL) {
			configs = Configs.General.OPTIONS;
		} else if (tab == ConfigGuiTab.TOGGLES) {
			configs = Configs.Toggles.TOGGLES;
		} else if (tab == ConfigGuiTab.CLIENTDATA) {
			configs = Configs.ClientData.OPTIONS;
		} else if (tab == ConfigGuiTab.KR1V) {
			configs = Configs.Kr1v.OPTIONS;
		} else {
			return Collections.emptyList();
		}

		ImmutableList.Builder<ConfigOptionWrapper> builder = ImmutableList.builder();
		for (IConfigBase config : configs) {
			if (config instanceof ConfigLabel)
				builder.add(new ConfigOptionWrapper(config.getComment()));
			else
				builder.add(new ConfigOptionWrapper(config));
		}
		return builder.build();
	}

	Method m;

	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
		if (this.client != null && this.client.world == null) this.renderPanoramaBackground(drawContext, partialTicks);
		this.applyBlur();
		super.render(drawContext, mouseX, mouseY, partialTicks);
	}

	static class ButtonListener implements IButtonActionListener {
		private final GuiConfigs parent;
		private final ConfigGuiTab tab;

		public ButtonListener(ConfigGuiTab tab, GuiConfigs parent) {
			this.tab = tab;
			this.parent = parent;
		}

		@Override
		public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
			GuiConfigs.tab = this.tab;
			this.parent.reCreateListWidget(); // apply the new config width
			if (this.parent.getListWidget() != null) this.parent.getListWidget().resetScrollbarPosition();
			this.parent.initGui();
		}
	}

	public enum ConfigGuiTab {
		GENERAL("General"),
		TOGGLES("Toggles"),
		CLIENTDATA("Chat"),
		MACROS("Macros"),
		KR1V("kr1v");

		private final String translationKey;

		ConfigGuiTab(String translationKey) {
			this.translationKey = translationKey;
		}

		public String getDisplayName() {
			return StringUtils.translate(this.translationKey);
		}
	}
}