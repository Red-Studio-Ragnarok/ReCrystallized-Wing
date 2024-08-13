package dev.redstudio.recrystallizedwing;

import dev.redstudio.recrystallizedwing.handlers.NostalgicSoundsHandler;
import dev.redstudio.recrystallizedwing.items.BurningWing;
import dev.redstudio.recrystallizedwing.items.BurntWing;
import dev.redstudio.recrystallizedwing.items.CrystalWing;
import dev.redstudio.recrystallizedwing.items.EnderScepter;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static dev.redstudio.recrystallizedwing.ProjectConstants.ID;
import static dev.redstudio.recrystallizedwing.ProjectConstants.NAME;
import static dev.redstudio.recrystallizedwing.ProjectConstants.VERSION;

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
@Mod.EventBusSubscriber
@Mod(modid = ID, name = NAME, version = VERSION, updateJSON = "https://forge.curseupdate.com/839163/" + ID)
public final class RCW {

    private static final Map<String, ResourceLocation> LOOT_TABLE_MAP = new HashMap<>();

    private static final ResourceLocation CRYSTAL_WING_LOW_TABLE = new ResourceLocation(ID, "chests/crystal_wing_low_loot");
    private static final ResourceLocation CRYSTAL_WING_HIGH_TABLE = new ResourceLocation(ID, "chests/crystal_wing_high_loot");
    private static final ResourceLocation ENDER_SCEPTER_TABLE = new ResourceLocation(ID, "chests/ender_scepter_loot");

    public static Item crystalWing, burningWing, burntWing, enderScepter;

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

    @Mod.EventHandler
    public static void init(final FMLInitializationEvent initializationEvent) {
        LootTableList.register(CRYSTAL_WING_LOW_TABLE);
        LootTableList.register(CRYSTAL_WING_HIGH_TABLE);
        LootTableList.register(ENDER_SCEPTER_TABLE);

        MinecraftForge.EVENT_BUS.register(NostalgicSoundsHandler.class);
    }

    @SubscribeEvent
    public static void lootTableLoad(final LootTableLoadEvent lootTableLoadEvent) {
        final ResourceLocation lootTableResourceLocation = LOOT_TABLE_MAP.get(lootTableLoadEvent.getName().toString());

        if (lootTableResourceLocation == null)
            return;

        final String lootTableName = lootTableResourceLocation.getPath() + "_loot";

        final LootEntry lootEntryTable = new LootEntryTable(lootTableResourceLocation, 1, 1, new LootCondition[0], lootTableName);
        final LootPool lootPool = new LootPool(new LootEntry[]{lootEntryTable}, new LootCondition[0], new RandomValueRange(1, 1), new RandomValueRange(1, 1), lootTableName);

        lootTableLoadEvent.getTable().addPool(lootPool);
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        crystalWing = new CrystalWing().setTranslationKey("crystal_wing").setRegistryName(ID, "crystal_wing");
        burningWing = new BurningWing().setTranslationKey("burning_wing").setRegistryName(ID, "burning_wing");
        burntWing = new BurntWing().setTranslationKey("burnt_wing").setRegistryName(ID, "burnt_wing");
        enderScepter = new EnderScepter().setTranslationKey("ender_scepter").setRegistryName(ID, "ender_scepter");

        itemRegistryEvent.getRegistry().registerAll(crystalWing, burningWing, burntWing, enderScepter);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRenders(final ModelRegistryEvent modelRegistryEvent) {
        ModelLoader.setCustomModelResourceLocation(crystalWing, 0, new ModelResourceLocation(crystalWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(burningWing, 0, new ModelResourceLocation(burningWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(burntWing, 0, new ModelResourceLocation(burntWing.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(enderScepter, 0, new ModelResourceLocation(enderScepter.delegate.name(), "inventory"));
    }
}
