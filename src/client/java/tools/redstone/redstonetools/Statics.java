package tools.redstone.redstonetools;

public class Statics {
	public static int currentDrawCalls = 0;
	public static int lastFrameDrawCalls = 0;
	public static int attemptedDrawCalls = 0;

	public static void increaseDrawCalls() {
		currentDrawCalls++;
	}

	public static boolean checkDrawCalls() {
		attemptedDrawCalls++;
		return Statics.currentDrawCalls >= Configs.Kr1v.MAX_DRAW_CALLS.getIntegerValue();
	}
}
