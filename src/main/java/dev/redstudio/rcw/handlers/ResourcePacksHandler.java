package dev.redstudio.rcw.handlers;

import lombok.NoArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

import static com.mojang.text2speech.Narrator.LOGGER;
import static dev.redstudio.rcw.ProjectConstants.ID;
import static dev.redstudio.rcw.ProjectConstants.NAME;
import static lombok.AccessLevel.PRIVATE;

/**
 * Event handler for adding resource packs.
 *
 * @author Luna Lage (Desoroxxx)
 * @see ModFilePackResources
 * @since 2.0
 */
@Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = PRIVATE)
public final class ResourcePacksHandler {

    /**
     * Taken from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/foundation/events/CommonEvents.java#L200">Create</a>
     */
    @SubscribeEvent
    public static void addPackFinders(final AddPackFindersEvent addPackFindersEvent) {
        if (addPackFindersEvent.getPackType() != PackType.CLIENT_RESOURCES)
            return;

        final IModFileInfo modFileInfo = ModList.get().getModFileById(ID);

        if (modFileInfo == null) {
            LOGGER.error("Could not find " + NAME + " mod file info; built-in resource packs will be missing!");
            return;
        }

        final IModFile modFile = modFileInfo.getFile();

        addResourcePack(addPackFindersEvent, modFile, "nostalgic_models", NAME + " Nostalgic Models");
        addResourcePack(addPackFindersEvent, modFile, "programmer_art", NAME + " Wing Programmer Art");
    }

    private static void addResourcePack(final AddPackFindersEvent addPackFindersEvent, final IModFile modFile, final String resourcePackPath, final String resourcePackName) {
        addPackFindersEvent.addRepositorySource(consumer -> consumer.accept(Pack.readMetaAndCreate(new ResourceLocation(ID, resourcePackPath).toString(), Component.literal(resourcePackName), false, id -> new ModFilePackResources(id, modFile, "resourcepacks/" + resourcePackPath), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT)));
    }

    /**
     * Taken from <a href="https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/foundation/ModFilePackResources.java">Create</a>
     */
    private static final class ModFilePackResources extends PathPackResources {

        private final IModFile modFile;
        private final String sourcePath;

        public ModFilePackResources(final String name, final IModFile modFile, final String sourcePath) {
            super(name, true, modFile.findResource(sourcePath));

            this.modFile = modFile;
            this.sourcePath = sourcePath;
        }

        @Override
        protected Path resolve(final String... paths) {
            final String[] allPaths = new String[paths.length + 1];

            allPaths[0] = sourcePath;

            System.arraycopy(paths, 0, allPaths, 1, paths.length);

            return modFile.findResource(allPaths);
        }
    }
}
