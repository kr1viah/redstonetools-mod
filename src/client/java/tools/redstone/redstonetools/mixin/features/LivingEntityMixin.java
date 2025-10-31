package tools.redstone.redstonetools.mixin.features;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tools.redstone.redstonetools.malilib.config.Configs;
import tools.redstone.redstonetools.utils.ChatUtils;

import java.util.*;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	private int jumpingCooldown;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Unique
	private LinkedList<Vec3d> positions = new LinkedList<>();

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void injectHead(CallbackInfo ci) {
		if (!(((Object)this) instanceof ClientPlayerEntity)) return;
		Vec3d pos = this.getPos();
		positions.add(pos);
		if (positions.size() > 10) {
			positions.removeFirst();
		}
	}

	@Definition(id = "isOnGround", method = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z")
	@Expression("this.isOnGround()")
	@Inject(method = "tickMovement", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 2))
	private void inject(CallbackInfo ci) {
		if (!(((Object)this) instanceof ClientPlayerEntity)) return;
		if (this.isOnGround() && this.jumpingCooldown == 0) {
			Vec3d pos = this.getPos();
			List<Box> boxes = getTouchingBoxes(pos, (int) Math.floor(pos.y - 0.00001));

			if (!boxes.isEmpty()) {
				double closestDistance = Double.MAX_VALUE;
				// find the closest box edge to player bounding box edge
				Box playerBox = this.getBoundingBox();
				for (Box box : boxes) {
					double distanceX = Math.min(Math.abs(playerBox.minX - box.maxX), Math.abs(playerBox.maxX - box.minX));
					double distanceZ = Math.min(Math.abs(playerBox.minZ - box.maxZ), Math.abs(playerBox.maxZ - box.minZ));
					double distance = Math.min(distanceX, distanceZ);
					if (distance < closestDistance) {
						closestDistance = distance;
					}
				}
				if (Configs.Kr1v.PRINT_SUBOPTIMAL_JUMPS.getBooleanValue())
					ChatUtils.sendMessage(String.format("Suboptimal jump by: %.4f blocks", closestDistance));
			} else {
				// idk man, unintended coyote time?
				if (Configs.Kr1v.PRINT_OPTIMAL_JUMPS.getBooleanValue())
					ChatUtils.sendMessage("You jumped on the last tick possible!");
			}

		}
		else if (!this.isOnGround() && this.jumpingCooldown == 0) {
			for (int i = 0; i < positions.size(); i++) {
				Vec3d pos = positions.get(positions.size() - 1 - i);
				List<Box> boxes = getTouchingBoxes(pos, (int) Math.floor(pos.y - 0.00001));
				if (!boxes.isEmpty()) {
					if (Configs.Kr1v.PRINT_MISSED_JUMPS.getBooleanValue())
						ChatUtils.sendMessage("You missed the jump by " + (i-1) + " tick(s)");
					break;
				}
			}
		}
	}

	@Unique
	private List<Box> getTouchingBoxes(Vec3d pos, int y) {
		double posY = pos.y;

		double minX = pos.x - 0.301;
		double maxX = pos.x + 0.301;
		double minZ = pos.z - 0.301;
		double maxZ = pos.z + 0.301;

		int xA = (int) Math.floor(minX);
		int xB = (int) Math.floor(maxX);
		int zA = (int) Math.floor(minZ);
		int zB = (int) Math.floor(maxZ);

		LinkedHashSet<BlockPos> positions = new LinkedHashSet<>();
		positions.add(new BlockPos(xA, y, zA));
		positions.add(new BlockPos(xA, y, zB));
		positions.add(new BlockPos(xB, y, zA));
		positions.add(new BlockPos(xB, y, zB));

		List<Box> boxes = new ArrayList<>();
		for (BlockPos blockPos : positions) {
			BlockState block = this.getWorld().getBlockState(blockPos);

			for (Box box : block.getCollisionShape(this.getWorld(), blockPos).getBoundingBoxes()) {
				double boxMinX = box.minX + blockPos.getX();
				double boxMaxX = box.maxX + blockPos.getX();
				double boxMinZ = box.minZ + blockPos.getZ();
				double boxMaxZ = box.maxZ + blockPos.getZ();
				double boxMinY = box.minY + blockPos.getY();
				double boxMaxY = box.maxY + blockPos.getY();

				if (boxMaxX >= minX && boxMinX <= maxX &&
					boxMaxZ >= minZ && boxMinZ <= maxZ &&
					boxMaxY == posY) {
					boxes.add(new Box(boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ));
				}
			}
		}

		return boxes;
	}
}
