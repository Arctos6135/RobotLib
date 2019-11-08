package com.arctos6135.robotlib.motors;

import com.arctos6135.robotlib.triggers.CurrentMonitoringTrigger;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * A motor with software overcurrent protection. The {@code ProtectedMotor} acts
 * as a wrapper around a normal motor.
 * <p>
 * When the maximum current allowed for this motor is exceeded for a specified
 * period of time, the motor becomes blacklisted. When a motor is blacklisted,
 * it will be set to 0 and will not respond to any operations. Once blacklisted,
 * a motor stays blacklisted unless it is overridden with
 * {@link #overrideBlacklist()}.
 * </p>
 * 
 * @author Tyler Tian
 */
public class ProtectedMotor {

    private Motor motor;

    // A motor is blacklisted when the current limit is exceeded for a set period of
    // time
    // After it is blacklisted, it will be set to 0 and will not respond to setting
    // Blacklist status will stick around forever unless cleared
    private boolean blacklisted = false;

    // Whether the protection system is enabled
    // If protection is not enabled, the motor will ignore overcurrent permanently
    private boolean enabled = true;

    /**
     * Creates a new protected motor object.
     * 
     * @param pdp             The PDP to get current readings from
     * @param channel         The PDP channel to get current readings from
     * @param motor           The internal {@link #Motor} object controlled
     * @param currentLimit    The current limit in amps
     * @param overcurrentTime The time allowed to exceed the current limit before
     *                        the motor is blacklisted
     * @param callback        A callback function to be run when the motor is
     *                        blacklisted
     */
    @SuppressWarnings("resource")
    public ProtectedMotor(PowerDistributionPanel pdp, int channel, Motor motor, double currentLimit,
            double overcurrentTime, Runnable callback) {
        this.motor = motor;
        
        new CurrentMonitoringTrigger(pdp, channel, currentLimit, overcurrentTime, () -> {
            if (enabled) {
                blacklisted = true;
                motor.set(0);

                if (callback != null) {
                    callback.run();
                }
            }
        });
    }

    /**
     * Creates a new protected motor object.
     * 
     * @param pdp             The PDP to get current readings from
     * @param channel         The PDP channel to get current readings from
     * @param motor           The internal {@link #Motor} object controlled
     * @param currentLimit    The current limit in amps
     * @param overcurrentTime The time allowed to exceed the current limit before
     *                        the motor is blacklisted
     */
    public ProtectedMotor(PowerDistributionPanel pdp, int channel, Motor motor, double currentLimit,
            double overcurrentTime) {
        this(pdp, channel, motor, currentLimit, overcurrentTime, null);
    }

    /**
     * Sets the motor. If the motor is blacklisted, this method will set it to 0
     * instead.
     * 
     * @param value The value to set the motor to
     */
    public void set(double value) {
        if (!blacklisted || !enabled) {
            motor.set(value);
        } else {
            motor.set(0);
        }
    }

    /**
     * Returns whether the motor is blacklisted.
     * 
     * @return Whether the motor is blacklisted
     */
    public boolean isBlacklisted() {
        return blacklisted;
    }

    /**
     * Overrides the blacklisted status of the motor. This is the only way to get
     * rid of blacklisted status.
     */
    public void clearBlacklist() {
        blacklisted = false;
    }

    /**
     * Manually sets the motor to be blacklisted.
     */
    public void blacklist() {
        blacklisted = true;
    }

    /**
     * Returns whether overcurrent protection is on for the motor.
     * 
     * <p>
     * If overcurrent protection is off, the motor will never be blacklisted, and
     * will still be controllable even if it is.
     * </p>
     * 
     * @return Whether overcurrent protection is on
     */
    public boolean getProtectionState() {
        return enabled;
    }

    /**
     * Enables or disables the overcurrent protection.
     * 
     * <p>
     * If overcurrent protection is off, the motor will never be blacklisted, and
     * will still be controllable even if it is.
     * </p>
     * 
     * @param enabled Whether overcurrent protection is on
     */
    public void setProtectionState(boolean enabled) {
        this.enabled = enabled;
    }
}
