package com.arctos6135.robotlib.oi.buttons;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * A {@code HeldButton} is a wrapper around a normal {@link Button} that needs
 * to be held down for a certain amount of time to activate.
 * 
 * @author Tyler Tian
 */
public class HeldButton extends Button {

    private Button button;
    private double duration;

    private double pressedAt = Double.NaN;

    /**
     * Creates a new {@link HeldButton}.
     * 
     * @param button   The button that needs to be held down
     * @param duration The duration it needs to be held down for (in seconds)
     */
    public HeldButton(Button button, double duration) {
        this.button = button;
        this.duration = duration;
    }


    @Override
    public boolean get() {
        // If button is pressed down:
        if (button.get()) {
            // Pressed down since is NaN (button not pressed down before), set the value
            if (Double.isNaN(pressedAt)) {
                pressedAt = Timer.getFPGATimestamp();
            }
            // Return whether the button has been pressed for more than the specified
            // duration
            return Timer.getFPGATimestamp() - pressedAt >= duration;
        } else {
            // If the button is not pressed, reset the last pressed down time
            pressedAt = Double.NaN;
            return false;
        }
    }
}
