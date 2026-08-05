package tools.redstone.redstonetools.utils;

/**
 * Runs work on the next server tick. The only platform-specific piece of the
 * incremental flood fill, and the first slice of a future platform layer.
 */
public class TickScheduler {

	//? if fabric {
	/*private static final java.util.Queue<Runnable> PENDING = new java.util.ArrayDeque<>();

	public static void init() {
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
			// Snapshot the size: a task that reschedules itself must not run twice in one tick.
			int count = PENDING.size();
			for (int i = 0; i < count; i++) {
				PENDING.poll().run();
			}
		});
	}

	public static void runNextTick(Runnable task) {
		PENDING.add(task);
	}
	*///? } else {
	private static org.bukkit.plugin.Plugin plugin;

	public static void init(org.bukkit.plugin.Plugin owner) {
		plugin = owner;
	}

	public static void runNextTick(Runnable task) {
		org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, task, 1L);
	}
	//? }
}
