package me.linus.momentum.managers;

import me.linus.momentum.util.combat.crystal.Crystal;
import me.linus.momentum.util.combat.crystal.CrystalPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * stores various information about AutoCrystal
 *
 * @author linustouchtips
 * @since 02/01/2021
 */

public class CrystalManager {

    public static List<CrystalPosition> placedCrystals = new ArrayList<>();
    public static List<Crystal> brokenCrystals = new ArrayList<>();
    public static int swings = 0;
    public static int placements = 0;
    public static int debugSwings = 0;
    public static int debugPlacements = 0;
    public static boolean skipTick = false;

    public static void updateSwings() {
        debugSwings++;
        swings++;
    }

    public static void updatePlacements() {
        debugPlacements++;
        placements++;
    }

    public static void resetCount() {
        swings = 0;
        placements = 0;
        debugSwings = 0;
        debugPlacements = 0;
    }

    public static void updateTicks(boolean in) {
        skipTick = in;
    }
}
