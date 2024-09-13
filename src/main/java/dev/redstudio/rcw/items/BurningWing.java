package dev.redstudio.rcw.items;

import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static dev.redstudio.rcw.RCW.BURNT_WING;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class BurningWing extends BaseItem {

    public BurningWing(final Properties properties) {
        super(properties, 1);
    }

    @Override
    public void inventoryTick(final ItemStack itemStack, final Level level, final Entity entity, final int itemSlot, final boolean flag) {
        if (level.isClientSide())
            return;

        if (entity.isInWater()) {
            if (!(entity instanceof Player))
                return;

            final Player player = (Player) entity;

            level.playSound(null, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.MASTER, 1, 1);
            RCWUtils.spawnExplosionParticleAtEntity(player, 160);

            player.getInventory().setItem(itemSlot, new ItemStack(BURNT_WING.get()));
        } else if (!entity.isOnFire()) {
            entity.igniteForSeconds(2);
        }
    }
}
