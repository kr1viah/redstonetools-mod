package tools.redstone.redstonetools.malilib.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraft.client.MinecraftClient;
import tools.redstone.redstonetools.RedstoneTools;
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
		public static final ConfigLabel 			SCREEN_LABEL = new ConfigLabel("Screen related configs");
		public static final ConfigBooleanHotkeyed 	DISABLED_SERVER_SCREEN_CLOSING = new ConfigBooleanHotkeyed("Prevent servers from closing the screen", false, "", "");
		public static final ConfigStringList 		DISABLED_SCREEN_CLOSING_EXCEPTIONS = new ConfigStringList("Only these", ImmutableList.of("ChatScreen"), "");
		public static final ConfigStringList 		PREVENT_OPENING_OF_SCREEN = new ConfigStringList("Prevent these screens from opening", ImmutableList.of(), "");

		public static final ConfigLabel 			CHAT_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			CHAT_LABEL = new ConfigLabel("Chat related configs");
		public static final ConfigStringList 		CHAT_HIDE = new ConfigStringList("Prevent messages with these regex strings from getting added to the chat", ImmutableList.of(), "");
		public static final ConfigBooleanHotkeyed 	REDIRECT_TO_SUBTITLES = new ConfigBooleanHotkeyed("Redirect matched messages to the subtitle hud element", true, "", "");
		public static final ConfigBooleanHotkeyed 	ALLOW_DUPLICATE_SUBTITLES = new ConfigBooleanHotkeyed("Duplicate subtitles", false, "", "Allow having multiple of the same message after each other in the subtitles");
		public static final ConfigBooleanHotkeyed 	CHAT_SELECTING = new ConfigBooleanHotkeyed("Chat selecting", true, "LEFT_CONTROL,C", "Be able to select and copy the chat");

		public static final ConfigLabel 			MISC_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			MISC_LABEL = new ConfigLabel("Miscellaneous related configs");
		public static final ConfigHotkey 			SHOW_CURSOR = new ConfigHotkey("Show cursor", "", "");
		public static final ConfigBooleanHotkeyed 	ALWAYS_CLOSE_BUTTON = new ConfigBooleanHotkeyed("Always close screens upon pressing escape", false, "", "");

		public static final ConfigLabel 			DEBUG_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			DEBUG_LABEL = new ConfigLabel("Debug related configs");
		public static final ConfigBooleanHotkeyed 	DISABLED_SERVER_SCREEN_CLOSING_PRINT = new ConfigBooleanHotkeyed("Print prevented screen closings", false, "", "");
		public static final ConfigBooleanHotkeyed 	PREVENT_OPENING_OF_SCREEN_PRINT = new ConfigBooleanHotkeyed("Print screen openings that aren't prevented", false, "", "");
		public static final ConfigBooleanHotkeyed 	PERCENTAGE_DROPPED_PACKETS_PRINT = new ConfigBooleanHotkeyed("Print prevented packets", false, "", "");

		public static final ConfigLabel 			PACKET_SEPARATOR = new ConfigLabel("");
		public static final ConfigLabel 			PACKET_LABEL = new ConfigLabel("Packet related configs");
		public static final ConfigBooleanHotkeyed 	AFFECT_PACKETS_C2S = new ConfigBooleanHotkeyed("Affect C2S packets", false, "", "");
		public static final ConfigBooleanHotkeyed 	AFFECT_PACKETS_S2C = new ConfigBooleanHotkeyed("Affect S2C packets", false, "", "");
		public static final ConfigStringList 		PACKETS_IGNORE = new ConfigStringList("Prevent these packets from getting affected", ImmutableList.of("HandshakeC2SPacket", "LoginHelloC2SPacket", "LoginSuccessS2CPacket", "EnterConfigurationC2SPacket", "CustomPayloadC2SPacket", "CustomPayloadS2CPacket", "ClientOptionsC2SPacket", "CommonPingS2CPacket", "CommonPongC2SPacket", "FeaturesS2CPacket", "SelectKnownPacksS2CPacket", "SelectKnownPacksC2SPacket", "DynamicRegistriesS2CPacket", "SynchronizeTagsS2CPacket", "ReadyS2CPacket", "ReadyC2SPacket"), "");
		public static final ConfigDouble  			PERCENTAGE_DROPPED_PACKETS = new ConfigDouble("Amount of packets to drop (0 = none, 1 = all)", 0, 0, 1, true, "");
		public static final ConfigDouble  			PERCENTAGE_DELAYED_PACKETS = new ConfigDouble("Amount of packets to lag (0 = none, 1 = all)", 0, 0, 1, true, "");
		public static final ConfigDouble  			PERCENTAGE_DELAYED_PACKETS_TIME = new ConfigDouble("Max lag (in ms)", 0, 0, 10000, true, "");

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

			CHAT_SELECTING.getKeybind().setCallback((button, keybind) -> {
				if (BetterChatHud.selectedText == null || BetterChatHud.selectedText.isEmpty() || !CHAT_SELECTING.getBooleanValue()) return false;
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
						}
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
			OPTIONS = ilb.build();

			for (IConfigBase config : OPTIONS) {
				if (config instanceof ConfigBooleanHotkeyed cbh) {
					if (((KeybindMulti) cbh.getKeybind()).getCallback() == null) {
						cbh.getKeybind().setCallback((keyAction, keybind) -> {
							cbh.setBooleanValue(!cbh.getBooleanValue());
							return true;
						});
					}
				}
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
