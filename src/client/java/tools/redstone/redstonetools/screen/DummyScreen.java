package tools.redstone.redstonetools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class DummyScreen extends Screen {
	public DummyScreen() {
		super(Text.of(""));
		client = MinecraftClient.getInstance();
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		assert client != null;
		client.setScreen(null);
		return true;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
