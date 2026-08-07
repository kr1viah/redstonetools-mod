package tools.redstone.redstonetools.utils;

import java.util.Locale;

public enum DirectionArgument {
	ME("m"),
	FORWARD("f"),
	BACK("b"),
	NORTH("n"),
	EAST("e"),
	SOUTH("s"),
	WEST("w"),
	NORTHEAST("ne"),
	NORTHWEST("nw"),
	SOUTHEAST("se"),
	SOUTHWEST("sw"),
	UP("u"),
	DOWN("d"),
	LEFT("l"),
	RIGHT("r");

	private final String alias;

	DirectionArgument(String alias) {
		this.alias = alias;
	}

	/** Accepts the full name or the WorldEdit style abbreviation, case insensitively. */
	public static DirectionArgument byNameOrAlias(String input) {
		for (DirectionArgument value : values()) {
			if (value.name().equalsIgnoreCase(input) || value.alias.equalsIgnoreCase(input)) {
				return value;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}