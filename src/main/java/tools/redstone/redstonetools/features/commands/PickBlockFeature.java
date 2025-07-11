package tools.redstone.redstonetools.features.commands;

import tools.redstone.redstonetools.features.feedback.Feedback;
import tools.redstone.redstonetools.utils.BlockInfo;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public abstract class PickBlockFeature extends BlockRaycastFeature {
    @Override
    protected final Feedback execute(ServerCommandSource source, @Nullable BlockInfo blockInfo) throws CommandSyntaxException {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return Feedback.error("Failed to get player.");
        }

        var stackOrFeedback = getItemStack(source, blockInfo);
        if (stackOrFeedback.right().isPresent()) {
            return stackOrFeedback.right().get();
        }

        assert stackOrFeedback.left().isPresent();
        var stack = stackOrFeedback.left().get();

        PlayerInventory playerInventory = client.player.getInventory();
        addPickBlock(playerInventory, stack);
        if (client.interactionManager == null) {
            throw new CommandSyntaxException(null, Text.of("Failed to get interaction manager."));
        }

        client.interactionManager.clickCreativeStack(client.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.getSelectedSlot());

        return Feedback.none();
    }

    // reimplementation from 1.18.2
    public void addPickBlock(PlayerInventory pi, ItemStack stack) {
        int i = pi.getSlotWithStack(stack);
        if (PlayerInventory.isValidHotbarIndex(i)) {
            pi.setSelectedSlot(i);
            return;
        }
        if (i == -1) {
            int j;
            pi.setSelectedSlot(pi.getSwappableHotbarSlot());
            if (!pi.getMainStacks().get(pi.getSelectedSlot()).isEmpty() && (j = pi.getEmptySlot()) != -1) {
                pi.getMainStacks().set(j, pi.getMainStacks().get(pi.getSelectedSlot()));
            }
            pi.getMainStacks().set(pi.getSelectedSlot(), stack);
        } else {
            pi.swapSlotWithHotbar(i);
        }
    }

    protected abstract Either<ItemStack, Feedback> getItemStack(ServerCommandSource source, @Nullable BlockInfo blockInfo) throws CommandSyntaxException;
}
