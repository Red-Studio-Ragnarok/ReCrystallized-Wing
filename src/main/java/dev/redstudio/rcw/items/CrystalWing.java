package dev.redstudio.rcw.items;

import dev.redstudio.rcw.RCW;
import dev.redstudio.rcw.config.RCWConfig;
import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class CrystalWing extends BaseItem {

    public CrystalWing() {
        super(RCWConfig.common.durability.crystalWingDurability);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (world.isRemote)
            return new ActionResult<>(EnumActionResult.PASS, itemStack);

        if (player.dimension == 0) {
            BlockPos targetLocation = player.getBedLocation(player.dimension);

            final IBlockState blockState = world.getBlockState(targetLocation);

            if (!blockState.getBlock().isBed(blockState, world, targetLocation, player)) {
                targetLocation = world.getSpawnPoint();

                final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(targetLocation);

                while (!RCWUtils.verifyTeleportCoordinates(world, mutablePos))
                    mutablePos.move(EnumFacing.SOUTH);

                targetLocation = mutablePos.toImmutable();
            } else {
                targetLocation = blockState.getBlock().getBedSpawnPosition(blockState, world, targetLocation, player);
            }

            player.sendStatusMessage(new TextComponentTranslation("teleport.chatMessage"), RCWConfig.common.showInActionBar);

            RCWUtils.teleportPlayer(world, player, targetLocation, 40);
        } else if (player.dimension == -1) {
            world.playSound(null, player.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemStack = new ItemStack(RCW.burningWing, 1);
        } else {
            RCWUtils.randomTeleport(world, player);
        }

        if (RCWConfig.common.durability.crystalWingDurability == 1) {
            itemStack.damageItem(2, player);
        } else if (RCWConfig.common.durability.crystalWingDurability > 0) {
            itemStack.damageItem(1, player);
        }

        player.getCooldownTracker().setCooldown(this, RCWConfig.common.cooldown.crystalWingCooldown);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    public EnumRarity getForgeRarity(final ItemStack itemStack) {
        return EnumRarity.UNCOMMON;
    }
}
