package dev.redstudio.recrystallizedwing.sounds;

import dev.redstudio.recrystallizedwing.handlers.NostalgicSoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Object representing a nostalgic sound from the original mod by DaftPVF for Minecraft 1.2.5.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 2.0
 */
public final class NostalgicSound {

    private final int notesStartTick;

    private final World world;
    private final EntityPlayer player;

    public NostalgicSound(final EntityPlayer player) {
        this.player = player;

        world = player.world;
        notesStartTick = player.ticksExisted;
    }

    /**
     * Plays the nostalgic sound and returns whether it has finished playing.
     *
     * @return {@code true} if this has finished playing, {@code false} otherwise
     * @see NostalgicSoundsHandler
     */
    public boolean play() {
        final int ticksSinceStart = player.ticksExisted - notesStartTick;

        switch (ticksSinceStart) {
            case 1:
                playPlingAtPitch(0.79F);
                break;
            case 5:
                playPlingAtPitch(1.18F);
                break;
            case 7:
                playPlingAtPitch(1.49F);
                break;
        }

        return ticksSinceStart > 7;
    }

    /**
     * Plays a short "pling" sound effect in the world with a specific pitch at the player location.
     *
     * @param pitch An integer value representing the pitch of the "pling" sound to be played, measured in semitones relative to the standard A440 tuning
     */
    private void playPlingAtPitch(final float pitch) {
        world.playSound(null, player.getPosition(), SoundEvents.BLOCK_NOTE_PLING, SoundCategory.MASTER, 0.5F, pitch);
    }
}
