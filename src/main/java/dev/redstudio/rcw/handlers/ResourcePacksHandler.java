package dev.redstudio.rcw.handlers;

import lombok.NoArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import static dev.redstudio.rcw.ProjectConstants.ID;
import static dev.redstudio.rcw.ProjectConstants.NAME;
import static lombok.AccessLevel.PRIVATE;

/**
 * Event handler for adding resource packs.
 *
 * @author Luna Lage (Desoroxxx)
 * @since 2.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class ResourcePacksHandler {

    @SubscribeEvent
    public static void addPackFinders(final AddPackFindersEvent addPackFindersEvent) {
        if (addPackFindersEvent.getPackType() != PackType.CLIENT_RESOURCES)
            return;

        addResourcePack(addPackFindersEvent, "nostalgic_models", NAME + " Nostalgic Models");
        addResourcePack(addPackFindersEvent, "programmer_art", NAME + " Wing Programmer Art");
    }

    private static void addResourcePack(final AddPackFindersEvent addPackFindersEvent, final String resourcePackPath, final String resourcePackName) {
        addPackFindersEvent.addPackFinders(ResourceLocation.fromNamespaceAndPath(ID, "resourcepacks/" + resourcePackPath), PackType.CLIENT_RESOURCES, Component.literal(resourcePackName), PackSource.DEFAULT, false, Pack.Position.TOP);
    }
}
