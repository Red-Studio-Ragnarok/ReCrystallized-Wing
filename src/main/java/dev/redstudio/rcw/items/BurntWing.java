package dev.redstudio.rcw.items;

import dev.redstudio.rcw.config.RCWConfig;
import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.redstudio.rcw.RCW.CRYSTAL_WING_ITEM;
import static net.minecraft.world.level.Level.NETHER;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class BurntWing extends BaseItem {

    public BurntWing(final Properties properties) {
        super(properties, RCWConfig.Common.BURNT_WING_DURABILITY.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide())
            return InteractionResultHolder.pass(itemStack);

        // If in the Nether, replace by a normal crystal wing and use it, which if in the nether will replace it by a burning crystal wing
        if (player.level().dimension() == NETHER)
            return CRYSTAL_WING_ITEM.get().use(level, player, hand);

        RCWUtils.randomTeleport(level, player);

        if (RCWConfig.Common.BURNT_WING_DURABILITY.get() == 1) {
            itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        } else if (RCWConfig.Common.BURNT_WING_DURABILITY.get() > 0) {
            itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        }

        player.getCooldowns().addCooldown(this, RCWConfig.Server.BURNT_WING_COOLDOWN.get());

        return InteractionResultHolder.success(itemStack);
    }
}
