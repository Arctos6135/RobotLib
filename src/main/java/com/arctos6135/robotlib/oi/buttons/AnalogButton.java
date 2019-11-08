package com.arctos6135.robotlib.oi.buttons;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The {@code AnalogButton} class is a {@link Button} that activates when the
 * value of some analog axis on a joystick exceeds a given threshold.
 * 
 * @author Tyler Tian
 */
public class AnalogButton extends Button {

    private int axis;
    private double threshold;
    private GenericHID joystick;
    private boolean reverse;

    private double pressedAt = Double.NaN;
    private double timeRequired = 0.1;

    /**
     * Creates a new {@link AnalogButton}.
     * 
     * @param joystick  The controller
     * @param trigger   The specific axis to read from
     * @param threshold The threshold that needs to be reached for this button to
     *                  activate
     */
    public AnalogButton(GenericHID joystick, int trigger, double threshold) {
        axis = trigger;
        this.joystick = joystick;
        this.threshold = threshold;
    }

    /**
     * Creates a new {@link AnalogButton}.
     * 
     * @param joystick  The controller
     * @param trigger   The specific axis to read from
     * @param threshold The threshold that needs to be reached for this button to
     *                  activate
     * @param reverse   If true, the value must be less than the threshold for the
     *                  button to activate
     */
    public AnalogButton(GenericHID joystick, int trigger, double threshold, boolean reverse) {
        axis = trigger;
        this.joystick = joystick;
        this.threshold = threshold;
        this.reverse = reverse;
    }

    /**
     * Sets the minimum amount of time the axis value is required to exceed the
     * threshold for before this button activates.
     * 
     * <p>
     * This minimum time requirement is to prevent the button from being activated
     * too many times due to an unstable analog reading.
     * </p>
     * 
     * @param required The minimum required time for activation, in seconds
     */
    public void setMinTimeRequired(double required) {
        timeRequired = required;
    }

    @Override
    public boolean get() {
        boolean exceeded = reverse ? joystick.getRawAxis(axis) <= threshold : joystick.getRawAxis(axis) >= threshold;
        // If button is pressed down:
        if (exceeded) {
            // Pressed down since is NaN (button not pressed down before), set the value
            if (Double.isNaN(pressedAt)) {
                pressedAt = Timer.getFPGATimestamp();
            }
            // Return whether the button has been pressed for more than the specified
            // duration
            return Timer.getFPGATimestamp() - pressedAt >= timeRequired;
        } else {
            // If the button is not pressed, reset the last pressed down time
            pressedAt = Double.NaN;
            return false;
        }
    }
}
