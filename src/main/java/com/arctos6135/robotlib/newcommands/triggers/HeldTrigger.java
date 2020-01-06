package com.arctos6135.robotlib.newcommands.triggers;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A {@code HeldTrigger} is a wrapper around a normal {@link Trigger} that needs
 * to remain active for a certain amount of time to activate.
 * 
 * @author Tyler Tian
 */
public class HeldTrigger extends Trigger {

    private Trigger trigger;
    private double duration;

    private double pressedAt = Double.NaN;

    /**
     * Creates a new {@link HeldTrigger}.
     * 
     * @param trigger  The trigger that needs to remain active
     * @param duration The duration it needs to remain active for (in seconds)
     */
    public HeldTrigger(Trigger trigger, double duration) {
        this.trigger = trigger;
        this.duration = duration;
    }

    @Override
    public boolean get() {
        // If trigger is pressed down:
        if (trigger.get()) {
            // Pressed down since is NaN (trigger not pressed down before), set the value
            if (Double.isNaN(pressedAt)) {
                pressedAt = Timer.getFPGATimestamp();
            }
            // Return whether the trigger has been pressed for more than the specified
            // duration
            return Timer.getFPGATimestamp() - pressedAt >= duration;
        } else {
            // If the trigger is not pressed, reset the last pressed down time
            pressedAt = Double.NaN;
            return false;
        }
    }
}
