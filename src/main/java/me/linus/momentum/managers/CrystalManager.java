package me.linus.momentum.managers;

import me.linus.momentum.util.combat.crystal.Crystal;
import me.linus.momentum.util.combat.crystal.CrystalPosition;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

public class CrystalManager {

    public static ConcurrentHashMap<CrystalPosition, Integer> placedCrystals = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Crystal, Integer> brokenCrystals = new ConcurrentHashMap<>();
    public static int swings = 0;
    public static boolean skipTick = false;

    public static void updateSwings() {
        swings++;
    }

    public static void updateTicks(boolean in) {
        skipTick = in;
    }
}
