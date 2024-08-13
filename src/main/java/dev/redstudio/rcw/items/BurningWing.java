package dev.redstudio.rcw.items;

import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import static dev.redstudio.rcw.RCW.burntWing;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class BurningWing extends BaseItem {

    @Override
    public void onUpdate(final ItemStack itemStack, final World world, final Entity entity, final int itemSlot, final boolean flag) {
        if (world.isRemote)
            return;

        if (entity.isInWater()) {
            if (!(entity instanceof EntityPlayer))
                return;

            final EntityPlayer player = (EntityPlayer) entity;

            world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.MASTER, 1, 1);
            RCWUtils.spawnExplosionParticleAtEntity(player, 160);

            player.inventory.setInventorySlotContents(itemSlot, new ItemStack(burntWing));
        } else if (!entity.isBurning()) {
            entity.setFire(2);
        }
    }
}
