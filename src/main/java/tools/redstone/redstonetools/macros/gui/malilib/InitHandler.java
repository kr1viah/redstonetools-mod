package tools.redstone.redstonetools.macros.gui.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;

public class InitHandler implements IInitializationHandler {
	@Override
	public void registerModHandlers()
	{
		ConfigManager.getInstance().registerConfigHandler("redstonetools", new Configs());
	}
}
