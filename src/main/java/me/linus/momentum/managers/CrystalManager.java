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
    public static boolean skipTick = false;

    public static void updateSwings() {
        swings++;
    }

    public static void updatePlacements() {
        placements++;
    }

    public static void updateTicks(boolean in) {
        skipTick = in;
    }
}
