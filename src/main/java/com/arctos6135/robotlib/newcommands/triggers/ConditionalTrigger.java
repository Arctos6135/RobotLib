package com.arctos6135.robotlib.newcommands.triggers;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * The {@code ConditionalTrigger} class is a wrapper around a normal
 * {@link Trigger} that can only be activated when its "condition" evaluates to
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
 * Trigger b = new ConditionalTrigger(new JoystickButton(joystick, number), allowInput);
 * b.whenActive(new SomeCommand()); // After this point, SomeCommand will run when the trigger is pressed
 * // ...
 * allowInput.set(false); // After this point, SomeCommand will not run, even if the trigger is pressed
 * </pre>
 * </p>
 * 
 * @author Tyler Tian
 */
public class ConditionalTrigger extends Trigger {

    private Trigger trigger;
    private AtomicBoolean condition;
    private boolean required = true;

    /**
     * Creates a new {@link ConditionalTrigger}.
     * 
     * <p>
     * This trigger can only be activated when {@code condition.get()} evaluates to
     * {@code true}.
     * </p>
     * 
     * @param trigger   The trigger to wrap around
     * @param condition The condition for this trigger
     */
    public ConditionalTrigger(Trigger trigger, AtomicBoolean condition) {
        this.trigger = trigger;
        this.condition = condition;
    }

    /**
     * Create a new {@link ConditionalTrigger}.
     * 
     * <p>
     * This trigger can only be activated when {@code condition.get() == required}.
     * </p>
     * 
     * @param trigger   The trigger to wrap around
     * @param condition The condition for this trigger
     * @param required  The state the condition is required to be in for this
     *                  trigger to activate
     */
    public ConditionalTrigger(Trigger trigger, AtomicBoolean condition, boolean required) {
        this(trigger, condition);
        this.required = required;
    }

    @Override
    public boolean get() {
        return trigger.get() && (condition.get() == required);
    }
}
