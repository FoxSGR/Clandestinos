package foxsgr.clandestinos.util;

import org.bukkit.enchantments.Enchantment;

/**
 * Enchantment related useful methods.
 */
@SuppressWarnings("unused")
public final class Enchantments {

    /**
     * Private constructor to hide the implicit public one.
     */
    private Enchantments() {
        // Should be empty.
    }

    /**
     * Finds an enchantment by its name.
     *
     * @param name the name of the enchantment.
     * @return the Enchantment or null if it doesn't exist.
     */
    public static Enchantment byName(String name) {
        name = name.toLowerCase();
        if (Comparisons.equalsOne(name, "sharpness", "damage", "damage_all")) {
            return Enchantment.DAMAGE_ALL;
        } else if (Comparisons.equalsOne(name, "unbreaking", "durability")) {
            return Enchantment.DURABILITY;
        } else if (Comparisons.startsAndEnds(name, "fire", "aspect")) {
            return Enchantment.FIRE_ASPECT;
        } else if (Comparisons.equalsOne(name, "smite", "damage_undead")) {
            return Enchantment.DAMAGE_UNDEAD;
        } else if (name.equalsIgnoreCase("knockback")) {
            return Enchantment.KNOCKBACK;
        } else if (name.equalsIgnoreCase("efficiency") || Comparisons.startsAndEnds(name, "dig", "speed")) {
            return Enchantment.DIG_SPEED;
        } else if (Comparisons.equalsOne(name, "fortune", "loot_bonus_blocks")) {
            return Enchantment.LOOT_BONUS_BLOCKS;
        } else if (Comparisons.equalsOne(name, "loot", "loot_bonus_mobs", "looting")) {
            return Enchantment.LOOT_BONUS_MOBS;
        } else if (Comparisons.equalsOne(name, "protection", "protection_environmental")) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }

        return null;
    }
}
