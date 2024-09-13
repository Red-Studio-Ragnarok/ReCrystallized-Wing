package dev.redstudio.rcw;

import dev.redstudio.rcw.config.RCWConfig;
import dev.redstudio.rcw.handlers.NostalgicSoundsHandler;
import dev.redstudio.rcw.handlers.ResourcePacksHandler;
import dev.redstudio.rcw.items.BurningWing;
import dev.redstudio.rcw.items.BurntWing;
import dev.redstudio.rcw.items.CrystalWing;
import dev.redstudio.rcw.items.EnderScepter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static dev.redstudio.rcw.ProjectConstants.ID;

//   /$$$$$$$             /$$$$$$                                  /$$               /$$ /$$ /$$                           /$$       /$$      /$$ /$$
//  | $$__  $$           /$$__  $$                                | $$              | $$| $$|__/                          | $$      | $$  /$ | $$|__/
//  | $$  \ $$  /$$$$$$ | $$  \__/  /$$$$$$  /$$   /$$  /$$$$$$$ /$$$$$$    /$$$$$$ | $$| $$ /$$ /$$$$$$$$  /$$$$$$   /$$$$$$$      | $$ /$$$| $$ /$$ /$$$$$$$   /$$$$$$
//  | $$$$$$$/ /$$__  $$| $$       /$$__  $$| $$  | $$ /$$_____/|_  $$_/   |____  $$| $$| $$| $$|____ /$$/ /$$__  $$ /$$__  $$      | $$/$$ $$ $$| $$| $$__  $$ /$$__  $$
//  | $$__  $$| $$$$$$$$| $$      | $$  \__/| $$  | $$|  $$$$$$   | $$      /$$$$$$$| $$| $$| $$   /$$$$/ | $$$$$$$$| $$  | $$      | $$$$_  $$$$| $$| $$  \ $$| $$  \ $$
//  | $$  \ $$| $$_____/| $$    $$| $$      | $$  | $$ \____  $$  | $$ /$$ /$$__  $$| $$| $$| $$  /$$__/  | $$_____/| $$  | $$      | $$$/ \  $$$| $$| $$  | $$| $$  | $$
//  | $$  | $$|  $$$$$$$|  $$$$$$/| $$      |  $$$$$$$ /$$$$$$$/  |  $$$$/|  $$$$$$$| $$| $$| $$ /$$$$$$$$|  $$$$$$$|  $$$$$$$      | $$/   \  $$| $$| $$  | $$|  $$$$$$$
//  |__/  |__/ \_______/ \______/ |__/       \____  $$|_______/    \___/   \_______/|__/|__/|__/|________/ \_______/ \_______/      |__/     \__/|__/|__/  |__/ \____  $$
//                                           /$$  | $$                                                                                                          /$$  \ $$
//                                          |  $$$$$$/                                                                                                         |  $$$$$$/
//                                           \______/                                                                                                           \______/
@Mod(ID)
public final class RCW {

    private static final Map<String, ResourceLocation> LOOT_TABLE_MAP = new HashMap<>();

    private static final ResourceLocation CRYSTAL_WING_LOW_TABLE = ResourceLocation.fromNamespaceAndPath(ID, "chests/crystal_wing_low_loot");
    private static final ResourceLocation CRYSTAL_WING_HIGH_TABLE = ResourceLocation.fromNamespaceAndPath(ID, "chests/crystal_wing_high_loot");
    private static final ResourceLocation ENDER_SCEPTER_TABLE = ResourceLocation.fromNamespaceAndPath(ID, "chests/ender_scepter_loot");

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(ID);

    public static final Supplier<Item> CRYSTAL_WING_ITEM = ITEMS.register("crystal_wing", () -> new CrystalWing(new CrystalWing.Properties()));
    public static final Supplier<Item> BURNING_WING = ITEMS.register("burning_wing", () -> new BurningWing(new BurningWing.Properties()));
    public static final Supplier<Item> BURNT_WING = ITEMS.register("burnt_wing", () -> new BurntWing(new BurntWing.Properties()));
    public static final Supplier<Item> ENDER_SCEPTER = ITEMS.register("ender_scepter", () -> new EnderScepter(new EnderScepter.Properties()));

    static {
        LOOT_TABLE_MAP.put("minecraft:chests/jungle_temple", CRYSTAL_WING_LOW_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/stronghold_library", CRYSTAL_WING_LOW_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/village_blacksmith", CRYSTAL_WING_LOW_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/spawn_bonus_chest", CRYSTAL_WING_LOW_TABLE);

        LOOT_TABLE_MAP.put("minecraft:chests/desert_pyramid", CRYSTAL_WING_HIGH_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/simple_dungeon", CRYSTAL_WING_HIGH_TABLE);
        LOOT_TABLE_MAP.put("minecraft:chests/nether_bridge", CRYSTAL_WING_HIGH_TABLE);

        LOOT_TABLE_MAP.put("minecraft:chests/end_city_treasure", ENDER_SCEPTER_TABLE);
    }

    public RCW(final IEventBus modEventBus, final ModContainer modContainer) {
        ITEMS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, RCWConfig.Client.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, RCWConfig.Common.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, RCWConfig.Server.SPEC);

        ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FMLPaths.CONFIGDIR.get());

        NeoForge.EVENT_BUS.register(NostalgicSoundsHandler.class);
        NeoForge.EVENT_BUS.register(RCW.class);

        modEventBus.register(ModBusEventListener.class);
        modEventBus.register(ResourcePacksHandler.class);
    }

    @SubscribeEvent
    public static void lootTableLoad(final LootTableLoadEvent lootTableLoadEvent) {
        final ResourceLocation lootTableResourceLocation = LOOT_TABLE_MAP.get(lootTableLoadEvent.getName().toString());

        if (lootTableResourceLocation == null)
            return;

        final LootPoolEntryContainer.Builder<?> entryBuilder = NestedLootTable.lootTableReference(ResourceKey.create(Registries.LOOT_TABLE, lootTableResourceLocation)).setQuality(1);

        final LootPool.Builder poolBuilder = new LootPool.Builder()
                .name(lootTableResourceLocation.getPath() + "_loot")
                .setRolls(UniformGenerator.between(1, 1))
                .setBonusRolls(UniformGenerator.between(1, 1))
                .add(entryBuilder);

        lootTableLoadEvent.getTable().addPool(poolBuilder.build());
    }

    private static class ModBusEventListener {

        @SubscribeEvent
        public static void onBuildContents(final BuildCreativeModeTabContentsEvent buildCreativeModeTabContentsEvent) {
            if (buildCreativeModeTabContentsEvent.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES)
                return;

            buildCreativeModeTabContentsEvent.accept(CRYSTAL_WING_ITEM.get());
            buildCreativeModeTabContentsEvent.accept(BURNING_WING.get());
            buildCreativeModeTabContentsEvent.accept(BURNT_WING.get());
            buildCreativeModeTabContentsEvent.accept(ENDER_SCEPTER.get());
        }
    }
}
