package tools.redstone.redstonetools.utils;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.formatting.component.PaginationBox;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.event.ClickEvent;
import com.sk89q.worldedit.util.formatting.text.event.HoverEvent;
import com.sk89q.worldedit.util.formatting.text.format.TextColor;

import java.util.List;

public class LocationsPaginationBox extends PaginationBox {

	private final List<BlockVector3> locations;

	public LocationsPaginationBox(List<BlockVector3> locations, String title, String pageCommand) {
		super(title, pageCommand);
		this.locations = locations;
		setComponentsPerPage(7);
	}

	@Override
	public Component getComponent(int number) {
		var pos = locations.get(number);
		return TextComponent.of((number + 1) + ": ")
			.append(TextComponent.of(WorldEditUtils.BV3ToString(pos)))
			.color(TextColor.LIGHT_PURPLE)
			.clickEvent(ClickEvent.runCommand("/tp " + pos.x() + " " + pos.y() + " " + pos.z()))
			.hoverEvent(HoverEvent.showText(TextComponent.of("Click to teleport")));
	}

	@Override
	public int getComponentsSize() {
		return locations.size();
	}
}
