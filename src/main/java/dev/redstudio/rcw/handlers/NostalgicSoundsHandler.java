package dev.redstudio.rcw.handlers;

import dev.redstudio.rcw.sounds.NostalgicSound;
import lombok.NoArgsConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * Event handler for playing nostalgic sounds.
 *
 * @author Luna Lage (Desoroxxx)
 * @see NostalgicSound
 * @since 2.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class NostalgicSoundsHandler {

    public static final List<NostalgicSound> NOSTALGIC_SOUNDS = new ArrayList<>();

    @SubscribeEvent
    public static void onTick(final LevelTickEvent.Pre levelTickEvent) {
        if (levelTickEvent.getLevel().isClientSide)
            return;

        NOSTALGIC_SOUNDS.removeIf(NostalgicSound::play);
    }
}
