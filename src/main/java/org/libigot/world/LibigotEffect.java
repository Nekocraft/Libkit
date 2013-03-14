package org.libigot.world;

public enum LibigotEffect {

    SMOKE("smoke"),
    BLOCK_BREAK("ironcrack_"),
    SNOWBALL_BREAK("snowballpoof"),
    TOOL_BREAK("tilecrack_"),
    PORTAL("portal"),
    SPLASH("splash"),
    BUBBLES("bubble"),
    MYCELIUM_SPORE("townaura"),
    EXPLOSION("hugeexplosion"),
    FLAME("flame"),
    HEART("heart"),
    CRITICAL_HIT_SPARK("magicCrit"),
    NOTE_BLOCK("note"),
    RUNES("enchantmenttable"),
    LAVA_SPARK("lava"),
    FOOT_STEPS("footstep"),
    REDSTONE_FUMES("reddust"),
    WATER_DROP("dripWater"),
    LAVA_DROP("dripLava"),
    SLIME_SPLATTER("slime");
    
    private final String name;

    LibigotEffect(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
