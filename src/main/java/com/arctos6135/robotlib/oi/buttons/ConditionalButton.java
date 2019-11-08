package com.arctos6135.robotlib.oi.buttons;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The {@code ConditionalButton} class is a wrapper around a normal
 * {@link Button} that can only be activated when its "condition" evaluates to
 * {@code true}.
 * 
 * <p>
 * The "condition" is passed in the form of an {@link AtomicBoolean}, so that it
 * can be modified outside of this class.
 * </p>
 * <p>
 * Example Usage:
 * 
 * <pre>
 * AtomicBoolean allowInput = new AtomicBoolean(true);
 * Button b = new ConditionalButton(new JoystickButton(joystick, buttonNumber), allowInput);
 * b.whenPressed(new SomeCommand()); // After this point, SomeCommand will run when the button is pressed
 * // ...
 * allowInput.set(false); // After this point, SomeCommand will not run, even if the button is pressed
 * </pre>
 * </p>
 * 
 * @author Tyler Tian
 */
public class ConditionalButton extends Button {

    private Button button;
    private AtomicBoolean condition;
    private boolean required = true;

    /**
     * Creates a new {@link ConditionalButton}.
     * 
     * <p>
     * This button can only be activated when {@code condition.get()} evaluates to
     * {@code true}.
     * </p>
     * 
     * @param button    The button to wrap around
     * @param condition The condition for this button
     */
    public ConditionalButton(Button button, AtomicBoolean condition) {
        this.button = button;
        this.condition = condition;
    }

    /**
     * Create a new {@link ConditionalButton}.
     * 
     * <p>
     * This button can only be activated when {@code condition.get() == required}.
     * </p>
     * 
     * @param button    The button to wrap around
     * @param condition The condition for this button
     * @param required  The state the condition is required to be in for this button
     *                  to activate
     */
    public ConditionalButton(Button button, AtomicBoolean condition, boolean required) {
        this(button, condition);
        this.required = required;
    }

    @Override
    public boolean get() {
        return button.get() && (condition.get() == required);
    }
}
