package tools.redstone.redstonetools;

//~ if paper 'net.fabricmc.api.ModInitializer' -> 'org.bukkit.plugin.java.JavaPlugin'
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.redstone.redstonetools.packets.RedstoneToolsPackets;

//~ if paper 'implements ModInitializer' -> 'extends JavaPlugin'
public class RedstoneTools extends JavaPlugin {
	public static final String MOD_ID = "redstonetools";
	public static final String MOD_NAME = "Redstone tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(RedstoneTools.MOD_ID);

	@Override
	//~ if paper 'onInitialize' -> 'onEnable'
	public void onEnable() {
		RedstoneToolsPackets.registerPackets();
		RedstoneToolsGameRules.register();
		Commands.registerCommands(/*? paper {*/this.getLifecycleManager()/*? }*/);

		//? paper
		getServer().getPluginManager().registerEvents(new RedstoneToolsListener(), this);
	}
}
