package dev.redstudio.recrystallizedwing.handlers;

import dev.redstudio.recrystallizedwing.sounds.NostalgicSound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Event handler for playing nostalgic sounds.
 *
 * @author Luna Lage (Desoroxxx)
 * @see NostalgicSound
 * @since 2.0
 */
public final class NostalgicSoundsHandler {

    public static final List<NostalgicSound> NOSTALGIC_SOUNDS = new ArrayList<>();

    @SubscribeEvent
    public static void onTick(final TickEvent.WorldTickEvent worldTickEvent) {
        if (worldTickEvent.side.isClient() || worldTickEvent.phase == TickEvent.Phase.START)
            return;

        NOSTALGIC_SOUNDS.removeIf(NostalgicSound::play);
    }
}
