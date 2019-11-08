package com.arctos6135.robotlib.triggers;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * A {@link Trigger} that monitors current on a PDP channel, activating when the
 * maximum current has been exceeded for a period of time.
 * 
 * @author Tyler Tian
 */
public class CurrentMonitoringTrigger extends Trigger {

    private PowerDistributionPanel pdp;
    private int channel;
    private double currentLimit;
    private double overcurrentTime;
    // NaN represents the current limit was never exceeded
    private double exceededAt = Double.NaN;

    private boolean enabled = true;

    /**
     * Creates a new current monitoring trigger.
     * 
     * @param pdp             The PDP to monitor current on
     * @param channel         The PDP channel to monitor current from
     * @param currentLimit    The maximum current limit, in amps
     * @param overcurrentTime The number of seconds of overcurrent allowed before
     *                        this trigger becomes active
     */
    public CurrentMonitoringTrigger(PowerDistributionPanel pdp, int channel, double currentLimit,
            double overcurrentTime) {
        this.pdp = pdp;
        this.channel = channel;
        this.currentLimit = currentLimit;
        this.overcurrentTime = overcurrentTime;
    }

    /**
     * Creates a new current monitoring trigger.
     * 
     * <p>
     * The callback is implemented with
     * {@link #whenActive(edu.wpi.first.wpilibj.command.Command)}
     * </p>
     * 
     * @param pdp             The PDP to monitor current on
     * @param channel         The PDP channel to monitor current from
     * @param currentLimit    The maximum current limit, in amps
     * @param overcurrentTime The number of seconds of overcurrent allowed before
     *                        this trigger becomes active
     * @param callback        A callback function to be run when this trigger is
     *                        active.
     */
    public CurrentMonitoringTrigger(PowerDistributionPanel pdp, int channel, double currentLimit,
            double overcurrentTime, Runnable callback) {
        this(pdp, channel, currentLimit, overcurrentTime);
        // Use whenActive and an anonymous InstantCommand to run the callback
        whenActive(new InstantCommand() {
            @Override
            public void initialize() {
                callback.run();
            }
        });
    }

    /**
     * Enables or disables this trigger.
     * 
     * @param enabled Whether the trigger is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets whether this trigger is enabled.
     * 
     * @return Whether this trigger is enabled
     */
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public boolean get() {
        if (!enabled) {
            return false;
        }

        // Grab the current and compare to see if the limit was exceeded
        double current = pdp.getCurrent(channel);
        if (current >= currentLimit) {
            // If the limit has not been exceeded before, update the exceeded time and
            // return false
            if (Double.isNaN(exceededAt)) {
                exceededAt = Timer.getFPGATimestamp();
                return false;
            }
            // If the limit as been exceeded before, and enough time has elapsed, return
            // true
            else if (Timer.getFPGATimestamp() - exceededAt >= overcurrentTime) {
                return true;
            } else {
                return false;
            }
        }
        // If limit not exceeded, reset the state and return false
        else {
            exceededAt = Double.NaN;
            return false;
        }
    }
}
