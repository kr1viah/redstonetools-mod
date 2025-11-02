package tools.redstone.redstonetools;

import javax.management.RuntimeErrorException;
import java.util.Random;

public class Statics {
	public static int currentDrawCalls = 0;
	public static int lastFrameDrawCalls = 0;
	public static int attemptedDrawCalls = 0;
	public static Random random = new Random(1230482134890L);
	public static boolean overrideAffect = false;
	private static final Random staticRandom = new Random();

	public static void increaseDrawCalls() {
		currentDrawCalls++;
	}

	public static boolean shouldAffect() {
		if (overrideAffect) return false;
		if (Configs.Kr1v.AFFECT_RENDERING.getBooleanValue())
			return true;
		return false;
	}

	public static boolean shouldRender() {
		attemptedDrawCalls++;
		if (!Configs.Kr1v.AFFECT_RENDERING.getBooleanValue())
			return true;
		if (Statics.currentDrawCalls >= Configs.Kr1v.MAX_DRAW_CALLS.getIntegerValue())
			return false;
		if (Statics.staticRandom.nextDouble() < Configs.Kr1v.DROP_X_DRAW_CALLS.getDoubleValue())
			return false;
		return true;
	}
}

