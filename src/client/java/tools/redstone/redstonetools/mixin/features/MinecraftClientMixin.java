package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.Configs;
import tools.redstone.redstonetools.Statics;
import tools.redstone.redstonetools.features.commands.OpenScreenFeature;
import tools.redstone.redstonetools.utils.ChatUtils;
import tools.redstone.redstonetools.utils.MappingUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(method = "<init>", at = @At("TAIL"))
	private static void collectScreenClasses(CallbackInfo ci) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forJavaClassPath())
			.addScanners(new SubTypesScanner(false))
		);
		Set<Class<? extends Screen>> allScreenClasses = reflections.getSubTypesOf(Screen.class);
		for (Class<? extends Screen> cls : allScreenClasses) {
			screenClasses.put(MappingUtils.intermediaryToYarnSimple(cls), cls);
			System.out.println("Registered screen class: " + MappingUtils.intermediaryToYarnSimple(cls));
		}
		System.out.println("Hello");
	}

	@Unique
	private static final Map<String, Class<? extends Screen>> screenClasses = new HashMap<>();
	// simple name, screen class
	@Shadow
	private boolean disconnecting;

	@Unique
	private int crashes = 0;

	@Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
	private void preventScreenOpening(Screen screen, CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (screen == null && this.disconnecting) {
			ci.cancel(); // prevent boom
		}
		if (Configs.Kr1v.preventClosingOnce) return;
		String currentScreenClass;
		if (screen == null) currentScreenClass = "null";
		else currentScreenClass = MappingUtils.intermediaryToYarnSimple(screen.getClass());
		for (Pair<String, String> s : Configs.Kr1v.PREVENT_OPENING_OF_SCREEN.getMap()) {
			if (s.getLeft().equals(currentScreenClass)) {
				ci.cancel();
				String newScreenClass = s.getRight();
				if (!newScreenClass.isEmpty()) {
					Screen newScreen = null;
					if (!newScreenClass.equals("null")) {
						Class<?> screenClass = screenClasses.get(newScreenClass);
						ChatUtils.sendMessage("Failed to create new instance of class: " + newScreenClass + ", will try saved instance / null-arg constructor / unsafe as last resort");
						if (OpenScreenFeature.INSTANCE.savedScreens.get(newScreenClass) != null) {
							newScreen = OpenScreenFeature.INSTANCE.savedScreens.get(newScreenClass);
							ChatUtils.sendMessage("Using saved screen instance for class: " + newScreenClass);
						} else {
							ChatUtils.sendMessage("No saved screen instance for class: \" + newScreenClass + \", trying to pass null to constructor arguments");
							try {
								@SuppressWarnings("unchecked")
								Constructor<Screen> constructor = (Constructor<Screen>) screenClass.getDeclaredConstructors()[0];
								constructor.setAccessible(true);
								Class<?>[] paramTypes = constructor.getParameterTypes();
								Object[] params = new Object[paramTypes.length];
								for (int i = 0; i < paramTypes.length; i++) {
									Class<?> paramType = paramTypes[i];

									if (paramType.isPrimitive()) {
										if (paramType == boolean.class) params[i] = false;
										else if (paramType == char.class) params[i] = '\0';
										else params[i] = 0; // byte, short, int, long, float, double
									} else if (paramType == String.class) {
										params[i] = "";
									} else if (PlayerInventory.class.isAssignableFrom(paramType)) {
										if (mc.player != null) {
											params[i] = mc.player.getInventory();
										} else {
											params[i] = new PlayerInventory(null, null);
										}
									} else if (Text.class.isAssignableFrom(paramTypes[i])) {
										params[i] = Text.literal("mrow :3");
									} else if (ItemStack.class.isAssignableFrom(paramTypes[i])) {
										params[i] = ItemStack.EMPTY;
									} else if (List.class.isAssignableFrom(paramTypes[i])) {
										params[i] = Collections.emptyList();
									} else if (MinecraftClient.class.isAssignableFrom(paramType)) {
										params[i] = mc;
									} else {
										for (Field field : getAllFields(MinecraftClient.class)) {
											if (paramType.isAssignableFrom(field.getType())) {
												field.setAccessible(true);
												try {
													params[i] = field.get(mc);
												} catch (IllegalAccessException e1) {
													params[i] = null;
												}
												break;
											}
										}
										if (params[i] == null) {
											for (Field field : getAllFields(ClientPlayerEntity.class)) {
												if (paramType.isAssignableFrom(field.getType())) {
													field.setAccessible(true);
													try {
														params[i] = field.get(mc.player);
													} catch (IllegalAccessException e1) {
														params[i] = null;
													}
													break;
												}
											}
										}
									}
								}
								newScreen = constructor.newInstance(params);
								ChatUtils.sendMessage("Successfully created new instance of class: " + newScreenClass + " via null constructor");
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException e3) {
								ChatUtils.sendMessage("Failed to create new instance of class: " + newScreenClass + " via null constructor");
							}
						}
					}
					mc.setScreen(newScreen);
				}
				return;
			}
		}
		if (Configs.Kr1v.PREVENT_OPENING_OF_SCREEN_PRINT.getBooleanValue())
			ChatUtils.sendMessage(Text.literal("Allowed screen opening of class: " + currentScreenClass + " (Click to copy)").setStyle(Style.EMPTY.withClickEvent(new ClickEvent.CopyToClipboard(currentScreenClass))));
		if (screen == null) return;
		OpenScreenFeature.INSTANCE.savedScreens.put(currentScreenClass, screen);
	}

	@Unique
	private static Field[] getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			Field[] declared = c.getDeclaredFields();
			Collections.addAll(fields, declared);
		}
		return fields.toArray(new Field[0]);
	}

	@WrapMethod(method = "render")
	private void injected(boolean tick, Operation<Void> original) {
		try {
			original.call(tick);
		} catch (Throwable t) {
			if (crashes++ > 100) throw t;
			RenderSystem.getModelViewStack().clear();
			RenderSystem.resetModelOffset();
			RenderSystem.resetTextureMatrix();
		}
		Statics.lastFrameDrawCalls = Statics.currentDrawCalls;
		Statics.currentDrawCalls = 0;
		Statics.attemptedDrawCalls = 0;
		Statics.random = new Random(1230482134890L);
	}
}
