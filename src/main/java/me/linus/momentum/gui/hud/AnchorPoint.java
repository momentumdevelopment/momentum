package me.linus.momentum.gui.hud;

import me.linus.momentum.mixin.MixinInterface;
import net.minecraft.client.gui.ScaledResolution;

public enum AnchorPoint implements MixinInterface {
    TopRight(new ScaledResolution(mc).getScaledWidth() - 2, 2),
    TopLeft(2, 2),
    BottomRight(new ScaledResolution(mc).getScaledWidth() - 2, new ScaledResolution(mc).getScaledHeight() - 2),
    BottomLeft(2, new ScaledResolution(mc).getScaledHeight() - 2),
    None(-1, -1);

    int x;
    int y;

    AnchorPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
