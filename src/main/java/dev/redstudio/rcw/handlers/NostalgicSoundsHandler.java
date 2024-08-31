package dev.redstudio.rcw.handlers;

import dev.redstudio.rcw.sounds.NostalgicSound;
import lombok.NoArgsConstructor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public static void onTick(final TickEvent.LevelTickEvent levelTickEvent) {
        if (levelTickEvent.side.isClient() || levelTickEvent.phase == TickEvent.Phase.START)
            return;

        NOSTALGIC_SOUNDS.removeIf(NostalgicSound::play);
    }
}
