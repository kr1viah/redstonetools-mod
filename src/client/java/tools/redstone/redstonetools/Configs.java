package tools.redstone.redstonetools;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.Vec3d;
import tools.redstone.redstonetools.malilib.config.ConfigLabel;
import tools.redstone.redstonetools.malilib.config.options.ConfigStringMap;
import tools.redstone.redstonetools.screen.BetterChatHud;
import tools.redstone.redstonetools.screen.DummyScreen;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Configs implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = RedstoneTools.MOD_ID + ".json";

	@SuppressWarnings("unused")
	public static class Kr1v {
		public static boolean preventClosingOnce = false;
		public static final ConfigLabel SCREEN_LABEL = new ConfigLabel("Screen related configs");
		public static final ConfigBooleanHotkeyed DISABLED_SERVER_SCREEN_CLOSING = new ConfigBooleanHotkeyed("Prevent servers from closing the screen", false, "", "");
		public static final ConfigStringList DISABLED_SCREEN_CLOSING_EXCEPTIONS = new ConfigStringList("Only these", ImmutableList.of("ChatScreen"), "");
		public static final ConfigStringMap PREVENT_OPENING_OF_SCREEN = new ConfigStringMap("Prevent these screens from opening", ImmutableList.of(), "", "", "");

		public static final ConfigLabel CHAT_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel CHAT_LABEL = new ConfigLabel("Chat related configs");
		public static final ConfigStringMap CHAT_REPLACE = new ConfigStringMap("Replace strings with strings", ImmutableList.of(), "Happens before hiding", "", "");
		public static final ConfigStringList CHAT_HIDE = new ConfigStringList("Prevent messages with these regex strings from getting added to the chat", ImmutableList.of(), "Happens after replacing");
		public static final ConfigBooleanHotkeyed REDIRECT_TO_SUBTITLES = new ConfigBooleanHotkeyed("Redirect matched messages to the subtitle hud element", true, "", "");
		public static final ConfigBooleanHotkeyed ALLOW_DUPLICATE_SUBTITLES = new ConfigBooleanHotkeyed("Duplicate subtitles", false, "", "Allow having multiple of the same message after each other in the subtitles");
		public static final ConfigBooleanHotkeyed CHAT_SELECTING = new ConfigBooleanHotkeyed("Chat selecting", true, "LEFT_CONTROL,C", KeybindSettings.GUI, "Be able to select and copy the chat");
		public static final ConfigColor CHAT_SELECTED_TEXT_BACKGROUND_COLOUR = new ConfigColor("Selected text background colour", "0xAA0033FF", "");

		public static final ConfigLabel 			KEY_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			KEY_LABEL = new ConfigLabel("Input related configs");
		public static final ConfigBooleanHotkeyed 	DISPLAY_CURRENTLY_PRESSED_KEYS = new ConfigBooleanHotkeyed("Display currently pressed keys", false, "", "");
		public static final ConfigBooleanHotkeyed 	DISPLAY_CURRENTLY_PRESSED_MOUSE_BUTTONS = new ConfigBooleanHotkeyed("Display currently pressed mouse buttons", false, "", "");
		public static final ConfigInteger 			PRESSED_KEYS_X = new ConfigInteger("Currently pressed keys X", 2, "");
		public static final ConfigInteger 			PRESSED_KEYS_Y = new ConfigInteger("Currently pressed keys Y", 0, "");
		public static final ConfigInteger 			PRESSED_MOUSE_X = new ConfigInteger("Currently pressed mouse X", 2, "");
		public static final ConfigInteger 			PRESSED_MOUSE_Y = new ConfigInteger("Currently pressed mouse Y", 11, "");

		public static final ConfigLabel 			RENDER_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			RENDER_LABEL = new ConfigLabel("Render related configs");
		public static final ConfigBooleanHotkeyed 	DONT_RENDER_HAND = new ConfigBooleanHotkeyed("Render hand", true, "", "Whether or not to render the player's hand");
		public static final ConfigBooleanHotkeyed	AFFECT_RENDERING = new ConfigBooleanHotkeyed("Affect rendering", false, "");
		public static final ConfigInteger 			MAX_DRAW_CALLS = new ConfigInteger("Max draw calls per frame", 100000, 0, 100000, "Maximum amount of draw calls allowed per frame before cancelling the rest");
		public static final ConfigDouble			DROP_X_DRAW_CALLS = new ConfigDouble("Drop x amount of draw calls", 0, 0, 1, "0 is none, 1 is all");
		public static final ConfigBooleanHotkeyed	RANDOMISE_X = new ConfigBooleanHotkeyed("Randomise X in vertexes", false, "", "");
		public static final ConfigFloat				X_FACTOR = new ConfigFloat("Factor to randomise x by", 1.0f, -100, 100);
		public static final ConfigBooleanHotkeyed	RANDOMISE_Y = new ConfigBooleanHotkeyed("Randomise Y in vertexes", false, "", "");
		public static final ConfigFloat				Y_FACTOR = new ConfigFloat("Factor to randomise y by", 1.0f, -100, 100);
		public static final ConfigBooleanHotkeyed	RANDOMISE_Z = new ConfigBooleanHotkeyed("Randomise Z in vertexes", false, "", "");
		public static final ConfigFloat				Z_FACTOR = new ConfigFloat("Factor to randomise z by", 1.0f, -100, 100);

		public static final ConfigLabel 			MISC_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			MISC_LABEL = new ConfigLabel("Miscellaneous related configs");
		public static final ConfigHotkey 			SHOW_CURSOR = new ConfigHotkey("Show cursor", "", "");
		public static final ConfigBooleanHotkeyed 	ALWAYS_CLOSE_BUTTON = new ConfigBooleanHotkeyed("Always close screens upon pressing escape", false, "", "");
		public static final ConfigHotkey 			FORCE_TOGGLE_FLIGHT = new ConfigHotkey("Force toggle creative flight", "", "");
		public static final ConfigBooleanHotkeyed 	PREVENT_FLIGHT_STATE_CHANGE = new ConfigBooleanHotkeyed("Prevent creative flight state change", false, "", "");
		public static final ConfigStringList 		QUICKPLAY_SERVERS = new ConfigStringList("Servers to put onto the main menu", ImmutableList.of(), "Separate name with ip with a #");
		public static final ConfigBooleanHotkeyed 	FAST_MAIN_MENU = new ConfigBooleanHotkeyed("Fast main menu", true, "", "");
		public static final ConfigBooleanHotkeyed 	PRINT_SUBOPTIMAL_JUMPS = new ConfigBooleanHotkeyed("Print suboptimal jumps", false, "", "Prints a message to the chat when a jump isn't made on the last tick possible");
		public static final ConfigBooleanHotkeyed 	PRINT_OPTIMAL_JUMPS = new ConfigBooleanHotkeyed("Print optimal jumps", false, "", "Prints a message to the chat when a jump is made on the last tick possible");
		public static final ConfigBooleanHotkeyed 	PRINT_MISSED_JUMPS = new ConfigBooleanHotkeyed("Print missed jumps", false, "", "Prints a message to the chat when a jump is missed");

		public static final ConfigLabel 			DEBUG_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			DEBUG_LABEL = new ConfigLabel("Debug related configs");
		public static final ConfigBooleanHotkeyed 	DISABLED_SERVER_SCREEN_CLOSING_PRINT = new ConfigBooleanHotkeyed("Print prevented screen closings", false, "", "");
		public static final ConfigBooleanHotkeyed 	PREVENT_OPENING_OF_SCREEN_PRINT = new ConfigBooleanHotkeyed("Print screen openings that aren't prevented", false, "", "");
		public static final ConfigBooleanHotkeyed 	PERCENTAGE_DROPPED_PACKETS_PRINT = new ConfigBooleanHotkeyed("Print prevented packets", false, "", "");

		public static final List<? extends IConfigBase> OPTIONS;

		static {
			ALWAYS_CLOSE_BUTTON.getKeybind().setCallback((keyAction, keybind) -> {
				preventClosingOnce = true;
				if (MinecraftClient.getInstance().currentScreen != null)
					MinecraftClient.getInstance().currentScreen.close();
				preventClosingOnce = false;
				return true;
			});

			SHOW_CURSOR.getKeybind().setCallback((button, keybind) -> {
				MinecraftClient.getInstance().setScreen(new DummyScreen());
				return true;
			});

			FORCE_TOGGLE_FLIGHT.getKeybind().setCallback((button, keybind) -> {
				ClientPlayerEntity player = MinecraftClient.getInstance().player;
				if (player != null && player.getAbilities().allowFlying) {
					player.getAbilities().flying = !player.getAbilities().flying;
					if (!PREVENT_FLIGHT_STATE_CHANGE.getBooleanValue() && player.isOnGround())
						player.addVelocity(new Vec3d(0, 0.08, 0));
				}
				return true;
			});

			CHAT_SELECTING.getKeybind().setCallback((button, keybind) -> {
				if (BetterChatHud.selectedText == null || BetterChatHud.selectedText.isEmpty() || !CHAT_SELECTING.getBooleanValue())
					return false;
				MinecraftClient.getInstance().keyboard.setClipboard(BetterChatHud.selectedText);
				return true;
			});

			var ilb = ImmutableList.<IConfigBase>builder();
			for (Field f : Kr1v.class.getDeclaredFields()) {
				int mods = f.getModifiers();
				if (Modifier.isStatic(mods) && IConfigBase.class.isAssignableFrom(f.getType())) {
					try {
						f.setAccessible(true);
						Object value = f.get(null);
						if (value != null) {
							ilb.add((IConfigBase) value);

							if (value instanceof ConfigBooleanHotkeyed cbh) {
								if (((KeybindMulti) cbh.getKeybind()).getCallback() == null) {
									cbh.getKeybind().setCallback((keyAction, keybind) -> {
										cbh.setBooleanValue(!cbh.getBooleanValue());
										return true;
									});
								}
							}
						}
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
			OPTIONS = ilb.build();

			//noinspection deprecation
			HudRenderCallback.EVENT.register((context, renderTickCounter) -> {
				renderKeysMouse(context, renderTickCounter);
				renderDrawCalls(context, renderTickCounter);
			});
		}

		private static void renderDrawCalls(DrawContext context, RenderTickCounter renderTickCounter) {

		}

		private static void renderKeysMouse(DrawContext context, RenderTickCounter renderTickCounter) {
			String toDisplay = KeybindMulti.getActiveKeysString();
			toDisplay = toDisplay.replaceAll("\\s*\\([^)]*\\)", "");
			if (toDisplay.equals("<none>")) return;
			var individualKeys = toDisplay.split("\\+");
			StringBuilder keyDisplay = new StringBuilder();
			StringBuilder mouseDisplay = new StringBuilder();
			boolean shouldNotAddPlusKey = true;
			boolean shouldNotAddPlusMouse = true;
			for (String s : individualKeys) {
				s = s.strip();
				if (s.startsWith("BUTTON_")) {
					if (!shouldNotAddPlusMouse) mouseDisplay.append(" + ");
					mouseDisplay.append(s);
					shouldNotAddPlusMouse = false;
				} else {
					if (!shouldNotAddPlusKey) keyDisplay.append(" + ");
					keyDisplay.append(s);
					shouldNotAddPlusKey = false;
				}
			}
			if (DISPLAY_CURRENTLY_PRESSED_KEYS.getBooleanValue()) {
				int x = Kr1v.PRESSED_KEYS_X.getIntegerValue();
				int y = Kr1v.PRESSED_KEYS_Y.getIntegerValue();
				context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, keyDisplay.toString(), x, y, 0xFFFFFFFF);
			}
			if (DISPLAY_CURRENTLY_PRESSED_MOUSE_BUTTONS.getBooleanValue()) {
				int x = Kr1v.PRESSED_MOUSE_X.getIntegerValue();
				int y = Kr1v.PRESSED_MOUSE_Y.getIntegerValue();
				context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, mouseDisplay.toString(), x, y, 0xFFFFFFFF);
			}
		}
	}

	public static class Toggles {
		public static final ConfigBooleanHotkeyed AIRPLACE = new ConfigBooleanHotkeyed("Airplace", false, "", "Whether or not airplace should be enabled");
		public static final ConfigBooleanHotkeyed AUTODUST = new ConfigBooleanHotkeyed("Autodust", false, "", "Whether or not autodust should be enabled");
		public static final ConfigBooleanHotkeyed AUTOROTATE = new ConfigBooleanHotkeyed("Autorotate", false, "", "Whether or not autorotate should be enabled");
		public static final ConfigBooleanHotkeyed BIGDUST = new ConfigBooleanHotkeyed("Bigdust", false, "", "Whether or not bigdust should be enabled");
		public static final ConfigBooleanHotkeyed CLICKCONTAINERS = new ConfigBooleanHotkeyed("Clickcontainers", false, "", "Whether or not clickcontainer should be enabled");

		public static final List<? extends IConfigBase> TOGGLES = List.of(
			AIRPLACE,
			AUTODUST,
			AUTOROTATE,
			BIGDUST,
			CLICKCONTAINERS
		);

		static {
			AIRPLACE.getKeybind().setCallback((t, g) -> {
				AIRPLACE.setBooleanValue(!AIRPLACE.getBooleanValue());
				return true;
			});
			AUTODUST.getKeybind().setCallback((t, g) -> {
				AUTODUST.setBooleanValue(!AUTODUST.getBooleanValue());
				return true;
			});
			AUTOROTATE.getKeybind().setCallback((t, g) -> {
				AUTOROTATE.setBooleanValue(!AUTOROTATE.getBooleanValue());
				return true;
			});
			BIGDUST.getKeybind().setCallback((t, g) -> {
				BIGDUST.setBooleanValue(!BIGDUST.getBooleanValue());
				return true;
			});
			CLICKCONTAINERS.getKeybind().setCallback((t, g) -> {
				CLICKCONTAINERS.setBooleanValue(!CLICKCONTAINERS.getBooleanValue());
				return true;
			});
		}
	}

	public static class ClientData {
		public static final ConfigBoolean ENABLE_MATH_VARIABLES = new ConfigBoolean("Enable math and variables for the chat input suggester", true,
			"""
				Whether or not to try to inject variables and math expressions into the command input suggester.
				
				With this enabled, Redstone tools will attempt to prevent chat suggestion from breaking if you're using variables and or math expressions inside of a command.
				With this disabled, variables and math expressions will still be inserted upon sending a chat command""");
		public static final ConfigString VARIABLE_BEGIN_STRING = new ConfigString("Variable begin string", "'", "The string that should be used to denote the start of a variable. Can be empty");
		public static final ConfigString VARIABLE_END_STRING = new ConfigString("Variable end string", "'", "The string that should be used to denote the end of a variable. Can be empty");
		public static final ConfigString MATH_BEGIN_STRING = new ConfigString("Math begin string", "{", "The string that should be used to denote the start of a math expression. Can be empty, unsure if you'd want that though.");
		public static final ConfigString MATH_END_STRING = new ConfigString("Math end string", "}", "The string that should be used to denote the end of a math expression. Can be empty, unsure if you'd want that though.");
		public static final ConfigString AUTORUN_FIRST_WORLD_ENTRY = new ConfigString("First world entry", "", "Command/message that will be run/sent the first time you join a world in this session");
		public static final ConfigString AUTORUN_WORLD_ENTRY = new ConfigString("World entry", "", "Command/message that will be run/sent when you join a world");
		public static final ConfigString AUTORUN_DIMENSION_CHANGE = new ConfigString("Dimension change", "", "Command/message that will be run/sent after you change dimensions");

		public static final List<IConfigBase> OPTIONS = new ArrayList<>();

		static {
			OPTIONS.add(ENABLE_MATH_VARIABLES);
			OPTIONS.add(VARIABLE_BEGIN_STRING);
			OPTIONS.add(VARIABLE_END_STRING);
			OPTIONS.add(MATH_BEGIN_STRING);
			OPTIONS.add(MATH_END_STRING);
			OPTIONS.add(AUTORUN_FIRST_WORLD_ENTRY);
			OPTIONS.add(AUTORUN_WORLD_ENTRY);
			OPTIONS.add(AUTORUN_DIMENSION_CHANGE);
		}
	}

	public static class General {
		public static final ConfigHotkey HOTKEY_OPEN_GUI = new ConfigHotkey("Hotkey to open menu", "V,C", "Hotkey to open menu");
		public static final ConfigBoolean BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS = new ConfigBoolean("Improved command suggestions", true,
			"""
				Enables/disables improved suggestions when typing commands.
				
				When typing "/give @s redstblock" in chat, with this disabled it will give no suggestions (default behaviour, or "prefix matching"), but with
				this enabled it will give "redstone_block" as a suggestion (so called "fuzzy matching").""");
		public static final ConfigBoolean AIRPLACE_SHOW_OUTLINE = new ConfigBoolean("Airplace showOutline", true, "If enabled, will show a block outline for the block your holding");
		public static final ConfigInteger BIGDUST_HEIGHT_IN_PIXELS = new ConfigInteger("Bigdust heightInPixels", 3, 0, 16, "How tall the redstone hitbox should be");
		public static final List<? extends IConfigBase> OPTIONS = List.of(
			HOTKEY_OPEN_GUI,
			BOOLEAN_IMPROVED_COMMAND_SUGGESTIONS,
			AIRPLACE_SHOW_OUTLINE,
			BIGDUST_HEIGHT_IN_PIXELS
		);

		public static int getHeightInPixels() {
			try {
				return BIGDUST_HEIGHT_IN_PIXELS.getIntegerValue();
			} catch (Exception ignored) {
				return 1;
			}
		}
	}

	public static void loadFromFile() {
		File configFile = new File(MinecraftClient.getInstance().runDirectory, "config/" + CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", General.OPTIONS);
				ConfigUtils.readConfigBase(root, "Toggles", Toggles.TOGGLES);
				ConfigUtils.readConfigBase(root, "ClientData", ClientData.OPTIONS);
				ConfigUtils.readConfigBase(root, "Kr1v", Kr1v.OPTIONS);
			}
		}
	}

	public static void saveToFile() {
		File dir = new File(MinecraftClient.getInstance().runDirectory, "config");

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", General.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Toggles", Toggles.TOGGLES);
			ConfigUtils.writeConfigBase(root, "ClientData", ClientData.OPTIONS);
			ConfigUtils.writeConfigBase(root, "Kr1v", Kr1v.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load() {
		loadFromFile();
	}

	@Override
	public void save() {
		saveToFile();
	}
}
