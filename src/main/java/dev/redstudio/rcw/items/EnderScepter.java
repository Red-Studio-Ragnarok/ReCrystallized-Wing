package dev.redstudio.rcw.items;

import dev.redstudio.rcw.config.RCWConfig;
import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class EnderScepter extends BaseItem {

    public EnderScepter(final Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON), RCWConfig.Common.ENDER_SCEPTER_DURABILITY.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide())
            return InteractionResultHolder.pass(itemStack);

        final HitResult rayTraceResult = RCWUtils.rayTraceWithExtendedReach(level, player);

        if (rayTraceResult == null || !rayTraceResult.getType().equals(HitResult.Type.BLOCK))
            return InteractionResultHolder.fail(itemStack);

        final BlockPos.MutableBlockPos target = BlockPos.containing(rayTraceResult.getLocation().x, rayTraceResult.getLocation().y, rayTraceResult.getLocation().z).mutable();

        if (player.getAbilities().flying)
            target.setY(Math.max((int) player.getY(), RCWUtils.getHighestSolidBlock(level, target, true)));

        RCWUtils.teleportPlayer(level, player, target, 40);

        if (RCWConfig.Common.ENDER_SCEPTER_DURABILITY.get() == 1)
            itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        else if (RCWConfig.Common.ENDER_SCEPTER_DURABILITY.get() > 0)
            itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));

        player.getCooldowns().addCooldown(this, RCWConfig.Server.ENDER_SCEPTER_COOLDOWN.get());

        return InteractionResultHolder.success(itemStack);
    }
}
