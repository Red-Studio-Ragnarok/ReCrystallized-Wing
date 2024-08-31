package dev.redstudio.rcw.items;

import dev.redstudio.rcw.config.RCWConfig;
import dev.redstudio.rcw.utils.RCWUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static dev.redstudio.rcw.ProjectConstants.ID;
import static dev.redstudio.rcw.RCW.BURNING_WING;
import static net.minecraft.world.level.Level.NETHER;
import static net.minecraft.world.level.Level.OVERWORLD;


/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public final class CrystalWing extends BaseItem {

    public CrystalWing(final Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON), RCWConfig.Common.CRYSTAL_WING_DURABILITY.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide())
            return InteractionResultHolder.pass(itemStack);

        if (player.getLevel().dimension() == OVERWORLD) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

            BlockPos targetLocation = serverPlayer.getRespawnPosition();
            Vec3 respawnPosition = null;

            if (targetLocation != null) {
                respawnPosition = Player.findRespawnPositionAndUseSpawnBlock((ServerLevel) level, targetLocation, serverPlayer.getRespawnAngle(), false, false).orElse(null);

                if (respawnPosition != null)
                    targetLocation = new BlockPos(respawnPosition);
            }

            if (targetLocation == null || respawnPosition == null) {
                targetLocation = level.getSharedSpawnPos();

                final BlockPos.MutableBlockPos mutablePos = targetLocation.mutable();

                while (!RCWUtils.verifyTeleportCoordinates(level, mutablePos))
                    mutablePos.move(Direction.SOUTH);

                targetLocation = mutablePos.immutable();
            }

            player.displayClientMessage(Component.translatable(ID + ".teleport.chatMessage"), RCWConfig.Client.SHOW_IN_ACTION_BAR.get());

            RCWUtils.teleportPlayer(level, player, targetLocation, 40);
        } else if (player.getLevel().dimension() == NETHER) {
            level.playSound(null, player.blockPosition(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            itemStack = new ItemStack(BURNING_WING.get(), 1);
        } else {
            RCWUtils.randomTeleport(level, player);
        }

        if (RCWConfig.Common.CRYSTAL_WING_DURABILITY.get() == 1) {
            itemStack.hurtAndBreak(2, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        } else if (RCWConfig.Common.CRYSTAL_WING_DURABILITY.get() > 0) {
            itemStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND  ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
        }

        player.getCooldowns().addCooldown(this, RCWConfig.Server.CRYSTAL_WING_COOLDOWN.get());

        return InteractionResultHolder.success(itemStack);
    }
}
