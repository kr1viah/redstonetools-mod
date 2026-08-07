package tools.redstone.redstonetools.features.logic;

import com.sk89q.worldedit.math.BlockVector3;
import tools.redstone.redstonetools.utils.DirectionArgument;

public record RStackArguments(int count, Step step, boolean expand, boolean withAir, boolean shiftSelection) {
	public static final int DEFAULT_COUNT = 1;
	public static final int DEFAULT_SPACING = 2;

	public sealed interface Step {
		record FromDirection(DirectionArgument direction, int spacing) implements Step {
		}

		record Explicit(BlockVector3 vector) implements Step {
		}
	}

	public static class ParseException extends Exception {
		public ParseException(String message) {
			super(message);
		}
	}

	public static RStackArguments parse(String input) throws ParseException {
		Integer count = null;
		Integer spacing = null;
		DirectionArgument direction = null;
		BlockVector3 vector = null;
		boolean expand = false;
		boolean withAir = false;
		boolean shiftSelection = false;

		for (String token : input.trim().split("\\s+")) {
			if (token.isEmpty()) {
				continue;
			}

			if (isFlags(token)) {
				for (char flag : token.substring(1).toCharArray()) {
					switch (flag) {
						case 'e' -> expand = true;
						case 'w' -> withAir = true;
						case 's' -> shiftSelection = true;
						default -> throw new ParseException("Unknown flag: -" + flag);
					}
				}
				continue;
			}

			if (token.indexOf(',') >= 0) {
				if (vector != null) {
					throw new ParseException("Only one vector can be given");
				}
				vector = parseVector(token);
				continue;
			}

			Integer number = tryParseInt(token);
			if (number != null) {
				if (count == null) {
					count = number;
				} else if (spacing == null) {
					spacing = number;
				} else {
					throw new ParseException("Too many numbers, expected a count and a spacing");
				}
				continue;
			}

			DirectionArgument parsed = DirectionArgument.byNameOrAlias(token);
			if (parsed == null) {
				throw new ParseException("Unknown argument: " + token);
			}
			if (direction != null) {
				throw new ParseException("Only one direction can be given");
			}
			direction = parsed;
		}

		if (vector != null && (direction != null || spacing != null)) {
			throw new ParseException("Give either a vector, or a direction and a spacing, not both");
		}

		Step step = vector != null
			? new Step.Explicit(vector)
			: new Step.FromDirection(
			direction == null ? DirectionArgument.ME : direction,
			spacing == null ? DEFAULT_SPACING : spacing);

		return new RStackArguments(count == null ? DEFAULT_COUNT : count, step, expand, withAir, shiftSelection);
	}

	private static boolean isFlags(String token) {
		return token.length() >= 2 && token.charAt(0) == '-' && Character.isLetter(token.charAt(1));
	}

	private static BlockVector3 parseVector(String token) throws ParseException {
		String[] parts = token.split(",", -1);
		if (parts.length != 3) {
			throw new ParseException("A vector is written x,y,z");
		}

		Integer x = tryParseInt(parts[0]);
		Integer y = tryParseInt(parts[1]);
		Integer z = tryParseInt(parts[2]);
		if (x == null || y == null || z == null) {
			throw new ParseException("A vector is written x,y,z with whole numbers");
		}

		return BlockVector3.at(x, y, z);
	}

	private static Integer tryParseInt(String token) {
		try {
			return Integer.valueOf(token);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}