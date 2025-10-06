package tools.redstone.redstonetools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Style;

public class DummyScreen extends ChatScreen {
	public DummyScreen() {
		super("");
		client = MinecraftClient.getInstance();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		this.client.inGameHud.getChatHud().render(context, this.client.inGameHud.getTicks(), mouseX, mouseY, true);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			ChatHud chatHud = this.client.inGameHud.getChatHud();
			if (chatHud.mouseClicked(mouseX, mouseY)) {
				return true;
			}

			Style style = this.client.inGameHud.getChatHud().getTextStyleAt(mouseX, mouseY);
			if (style != null && this.handleTextClick(style)) {
				return true;
			}
		}
		this.client.setScreen(null);
		return true;
	}
}
