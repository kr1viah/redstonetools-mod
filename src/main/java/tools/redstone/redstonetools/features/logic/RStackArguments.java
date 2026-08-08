package tools.redstone.redstonetools.features.logic;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.Direction;
import tools.redstone.redstonetools.utils.DirectionArgument;
import tools.redstone.redstonetools.utils.DirectionUtils;

public record RStackArguments(int count, BlockVector3 vector, boolean expand, boolean withAir, boolean shiftSelection) {
	public static final int DEFAULT_COUNT = 1;
	public static final int DEFAULT_SPACING = 2;

	public static class ParseException extends Exception {
		public ParseException(String message) {
			super(message);
		}
	}

	public static RStackArguments parse(String input, Direction playerFacing) throws ParseException {
		Integer count = null;
		Integer spacing = null;
		DirectionArgument direction = null;
		BlockVector3 explicitVector = null;
		boolean expand = false;
		boolean withAir = false;
		boolean shiftSelection = false;

		for (String token : input.trim().split("\\s+")) {
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
				if (explicitVector != null) {
					throw new ParseException("Only one vector can be given");
				}
				explicitVector = parseVector(token);
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

		if (explicitVector != null && (direction != null || spacing != null)) {
			throw new ParseException("Give either a vector, or a direction and a spacing, not both");
		}

		return new RStackArguments(
			count == null ? DEFAULT_COUNT : count,
			explicitVector != null ? explicitVector : toVector(direction, spacing, playerFacing),
			expand,
			withAir,
			shiftSelection);
	}

	/** A direction and a spacing are just a way of writing the vector between two copies. */
	private static BlockVector3 toVector(DirectionArgument direction, Integer spacing, Direction playerFacing) throws ParseException {
		Direction resolved;

		try {
			resolved = DirectionUtils.matchDirection(direction == null ? DirectionArgument.ME : direction, playerFacing);
		} catch (Exception ex) {
			throw new ParseException(ex.getMessage());
		}

		BlockVector3 unit = DirectionUtils.directionToBlock(resolved);
		if (unit == null) {
			throw new ParseException("Unsupported direction: " + resolved);
		}

		return unit.multiply(spacing == null ? DEFAULT_SPACING : spacing);
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