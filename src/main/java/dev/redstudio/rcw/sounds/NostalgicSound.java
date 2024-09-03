package dev.redstudio.rcw.sounds;

import dev.redstudio.rcw.handlers.NostalgicSoundsHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Object representing a nostalgic sound from the original mod by DaftPVF for Minecraft 1.2.5.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 2.0
 */
public final class NostalgicSound {

    private final int notesStartTick;

    private final Level level;
    private final Player player;

    public NostalgicSound(final Player player) {
        this.player = player;

        level = player.level();
        notesStartTick = player.tickCount;
    }

    /**
     * Plays the nostalgic sound and returns whether it has finished playing.
     *
     * @return {@code true} if this has finished playing, {@code false} otherwise
     *
     * @see NostalgicSoundsHandler
     */
    public boolean play() {
        final int ticksSinceStart = player.tickCount - notesStartTick;

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
        level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.MASTER, 0.5F, pitch);
    }
}
