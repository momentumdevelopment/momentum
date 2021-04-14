package me.linus.momentum.module.modules.client;

import me.linus.momentum.gui.main.GUIScreen;
import me.linus.momentum.managers.ScreenManager;
import me.linus.momentum.module.Module;
import me.linus.momentum.setting.checkbox.Checkbox;
import me.linus.momentum.setting.checkbox.SubCheckbox;
import me.linus.momentum.setting.slider.Slider;
import me.linus.momentum.setting.slider.SubSlider;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * @author bon & linustouchtips
 * @since 11/16/2020
 */

public class ClickGUI extends Module {
	public ClickGUI() {
		super("ClickGUI", Category.CLIENT, "Opens the ClickGUI");
		this.getKeybind().setKeyCode(Keyboard.KEY_COMMA);
	}

	public static Checkbox animations = new Checkbox("Animations", true);
	public static SubSlider animationSpeed = new SubSlider(animations, "Animation Speed", 0.0D, 300.0D, 500.0D, 0);

	public static Checkbox sounds = new Checkbox("Sounds", true);
	public static SubSlider pitch = new SubSlider(sounds, "Pitch", 0.0D, 1.0D, 1.0D, 2);

	public static Slider scrollSpeed = new Slider("Scroll Speed", 0.0D, 10.0D, 20.0D, 0);

	public static Checkbox blurEffect = new Checkbox("Blur", true);
	public static Checkbox allowOverflow = new Checkbox("Allow Overflow", true);

	public static Checkbox snapSlider = new Checkbox("Slider Snap", true);
	public static SubSlider snapSub = new SubSlider(snapSlider, "Snap Distance", 1.0D, 5.0D, 10.0D, 0);

	public static Checkbox navigation = new Checkbox("Navigation", true);
	public static SubCheckbox descriptions = new SubCheckbox(navigation, "Descriptions", true);

	public static Checkbox outline = new Checkbox("Outline", true);
	public static Checkbox indicators = new Checkbox("Indicators", true);
	public static Checkbox pauseGame = new Checkbox("Pause Game", false);

	@Override
	public void setup() {
		addSetting(animations);
		addSetting(sounds);
		addSetting(scrollSpeed);
		addSetting(blurEffect);
		addSetting(snapSlider);
		addSetting(allowOverflow);
		addSetting(navigation);
		addSetting(outline);
		addSetting(indicators);
		addSetting(pauseGame);
	}

	GUIScreen clickGui = new GUIScreen();

	@Override
	public void onEnable() {
		if (nullCheck())
			return;

		super.onEnable();
		ScreenManager.setScreen(clickGui);
	}

	@Override
	public void onUpdate() {
		if (nullCheck()) {
			this.disable();
		}
	}
}