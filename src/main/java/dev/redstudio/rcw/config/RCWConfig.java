package dev.redstudio.rcw.config;

import lombok.NoArgsConstructor;
import net.minecraftforge.common.ForgeConfigSpec;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Luna Lage (Desoroxxx)
 * @since 1.0
 */
public class RCWConfig {

    @NoArgsConstructor(access = PRIVATE)
    public class Client {

        public static final ForgeConfigSpec SPEC;

        private static final ForgeConfigSpec.Builder BUILDER;

        public static final ForgeConfigSpec.BooleanValue SHOW_IN_ACTION_BAR;

        static {
            BUILDER = new ForgeConfigSpec.Builder();

            SHOW_IN_ACTION_BAR = BUILDER
                    .comment("If true show the message when you use the Crystal Wing in the action bar instead of the chat")
                    .define("showInActionBar", true);

            SPEC = BUILDER.build();
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public class Common {

        public static final ForgeConfigSpec SPEC;

        private static final ForgeConfigSpec.Builder BUILDER;

        // Durability
        public static final ForgeConfigSpec.IntValue CRYSTAL_WING_DURABILITY;
        public static final ForgeConfigSpec.IntValue BURNT_WING_DURABILITY;
        public static final ForgeConfigSpec.IntValue ENDER_SCEPTER_DURABILITY;

        static {
            BUILDER = new ForgeConfigSpec.Builder();

            BUILDER.push("Durability").comment("Configuration for the durability of items");

            CRYSTAL_WING_DURABILITY = BUILDER
                    .comment("Max durability of the Crystal Wing, 0 means infinite durability.")
                    .defineInRange("crystalWingDurability", 32, 0, Integer.MAX_VALUE);

            BURNT_WING_DURABILITY = BUILDER
                    .comment("Max durability of the Burnt Wing, 0 means infinite durability.")
                    .defineInRange("BurntWingDurability", 16, 0, Integer.MAX_VALUE);

            ENDER_SCEPTER_DURABILITY = BUILDER
                    .comment("Max durability of the Ender Scepter, 0 means infinite durability.")
                    .defineInRange("enderScepterDurability", 128, 0, Integer.MAX_VALUE);

            BUILDER.pop();

            SPEC = BUILDER.build();
        }
    }

    @NoArgsConstructor(access = PRIVATE)
    public class Server {

        public static final ForgeConfigSpec SPEC;

        private static final ForgeConfigSpec.Builder BUILDER;

        // General
        public static final ForgeConfigSpec.BooleanValue NOSTALGIC_SOUNDS;
        public static final ForgeConfigSpec.IntValue ENDER_SCEPTER_REACH;
        public static final ForgeConfigSpec.IntValue ENDER_SCEPTER_CREATIVE_REACH_MULT;
        public static final ForgeConfigSpec.IntValue RANDOM_TELEPORTATION_DISTANCE;

        // Cooldown
        public static final ForgeConfigSpec.IntValue CRYSTAL_WING_COOLDOWN;
        public static final ForgeConfigSpec.IntValue BURNT_WING_COOLDOWN;
        public static final ForgeConfigSpec.IntValue ENDER_SCEPTER_COOLDOWN;

        static {
            BUILDER = new ForgeConfigSpec.Builder();

            NOSTALGIC_SOUNDS = BUILDER
                    .comment("If true uses the sounds from the original Crystal Wing 1.2.5")
                    .define("nostalgicSounds", false);

            ENDER_SCEPTER_REACH = BUILDER
                    .comment("Ender Scepter reach, in blocks")
                    .defineInRange("enderScepterReach", 60, 0, Integer.MAX_VALUE);

            ENDER_SCEPTER_CREATIVE_REACH_MULT = BUILDER
                    .comment("Ender Scepter creative reach multiplier")
                    .defineInRange("enderScepterCreativeReachMult", 4, 0, Integer.MAX_VALUE);

            RANDOM_TELEPORTATION_DISTANCE = BUILDER
                    .comment("Random teleportation distance. determines how far should the player be teleported when random teleportation is used (Using a Crystal Wing in the end or using a Burnt Wing), in blocks")
                    .defineInRange("randomTeleportationDistance", 1000, 0, Integer.MAX_VALUE);

            BUILDER.push("Cooldown").comment("Configuration for the cooldown of items in ticks");

            CRYSTAL_WING_COOLDOWN = BUILDER
                    .comment("Cooldown between uses of the Crystal Wing in ticks")
                    .defineInRange("crystalWingCooldown", 256, 0, Integer.MAX_VALUE);

            BURNT_WING_COOLDOWN = BUILDER
                    .comment("Cooldown between uses of the Burnt Wing in ticks")
                    .defineInRange("BurntWingCooldown", 512, 0, Integer.MAX_VALUE);

            ENDER_SCEPTER_COOLDOWN = BUILDER
                    .comment("Cooldown between uses of the Ender Scepter in ticks")
                    .defineInRange("enderScepterCooldown", 20, 0, Integer.MAX_VALUE);

            BUILDER.pop();

            SPEC = BUILDER.build();
        }
    }
}
