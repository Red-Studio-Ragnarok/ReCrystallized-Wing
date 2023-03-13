package io.redstudioragnarok.recrystallizedwing.items;

import io.redstudioragnarok.recrystallizedwing.RCW;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.Random;

import static io.redstudioragnarok.recrystallizedwing.RCW.crystalWing;

public class CrystalWingBurnt extends Item {

    private final Random random = new Random();

    public CrystalWingBurnt() {
        setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!world.isRemote) {
            // If in the nether replace by a normal crystal wing and use it, which if in the nether will replace it by a burning crystal wing
            if (player.dimension == -1)
                return crystalWing.onItemRightClick(world, player, hand);

            RCW.randomTeleport(world, player);

            RCW.spawnExplosionParticleAtEntity(player, world, 80);
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}
