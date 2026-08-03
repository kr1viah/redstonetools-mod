package tools.redstone.redstonetools;

//~ if paper 'net.fabricmc.api.ModInitializer' -> 'org.bukkit.plugin.java.JavaPlugin'
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
		registerNetworking();
		registerGameRules();
		registerCommands();
		registerListeners();
	}

	/** Feature toggle sync with the client mod. Fabric uses custom payloads, Paper plugin messaging. */
	private void registerNetworking() {
		//? if fabric {
		/*RedstoneToolsPackets.registerPackets();
		 *///? } else {
		RedstoneToolsPackets.registerPackets(this);
		//? }
	}

	/**
	 * Vanilla registries are frozen before plugins are enabled, so custom game rules
	 * cannot be registered on Paper. doContainerDrops still needs a Bukkit-side
	 * reimplementation, most likely through BlockDropItemEvent.
	 */
	private void registerGameRules() {
		//? if fabric {
		/*RedstoneToolsGameRules.register();
		*///? }
	}

	private void registerCommands() {
		//? if fabric {
		/*Commands.registerCommands();
		*///? } else {
		Commands.registerCommands(this.getLifecycleManager());
		//? }
	}

	/** Feature hooks. Paper only: Fabric registers its callbacks statically. */
	private void registerListeners() {
		//? if paper {
		getServer().getPluginManager().registerEvents(new RedstoneToolsListener(), this);
		//? }
	}
}
