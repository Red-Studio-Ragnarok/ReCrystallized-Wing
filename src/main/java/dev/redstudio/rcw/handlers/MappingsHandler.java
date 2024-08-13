package dev.redstudio.rcw.handlers;

import dev.redstudio.rcw.RCW;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static lombok.AccessLevel.PRIVATE;

/**
 * Event handler for remapping item after the switch of ID.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 2.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class MappingsHandler {

    @SubscribeEvent
    public static void onRegistryEvent(final RegistryEvent.MissingMappings<Item> registryEvent) {
        for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : registryEvent.getAllMappings()) {
            if (!mapping.key.getNamespace().equals("recrystallizedwing"))
                continue;

            switch (mapping.key.getPath()) {
                case "crystal_wing":
                    mapping.remap(RCW.crystalWing);
                    break;
                case "burning_wing":
                    mapping.remap(RCW.burningWing);
                    break;
                case "burnt_wing":
                    mapping.remap(RCW.burntWing);
                    break;
                case "ender_scepter":
                    mapping.remap(RCW.enderScepter);
                    break;
            }
        }
    }
}
