package com.x404dev.astralQOL.qol.antilogstripper;

import org.bukkit.Material;

import java.util.Set;

public class Strippable {

    private static final Set<Material> AXES = Set.of(
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.GOLDEN_AXE,
            Material.IRON_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
    );

    private static final Set<Material> STRIPPABLE_BLOCKS = Set.of(
            Material.ACACIA_LOG,
            Material.ACACIA_WOOD,
            Material.BAMBOO_BLOCK,
            Material.BIRCH_LOG,
            Material.BIRCH_WOOD,
            Material.CHERRY_LOG,
            Material.CHERRY_WOOD,
            Material.DARK_OAK_LOG,
            Material.DARK_OAK_WOOD,
            Material.JUNGLE_LOG,
            Material.JUNGLE_WOOD,
            Material.MANGROVE_LOG,
            Material.MANGROVE_WOOD,
            Material.OAK_LOG,
            Material.OAK_WOOD,
            Material.SPRUCE_LOG,
            Material.SPRUCE_WOOD,
            Material.WARPED_STEM,
            Material.WARPED_HYPHAE,
            Material.CRIMSON_STEM,
            Material.CRIMSON_HYPHAE
    );

    public static boolean isAxe(Material material) {
        return AXES.contains(material);
    }

    public static boolean isStrippableBlock(Material material) {
        return STRIPPABLE_BLOCKS.contains(material);
    }

    private Strippable() {}
}
