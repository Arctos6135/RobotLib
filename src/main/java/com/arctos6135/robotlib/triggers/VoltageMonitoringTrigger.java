package com.arctos6135.robotlib.triggers;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * A {@link Trigger} that monitors the robot's battery voltage and triggers when
 * it lowers below a certain threshold.
 * 
 * @author Tyler Tian
 */
public class VoltageMonitoringTrigger extends Trigger {

    private final double threshold;
    private final double buffer;

    private boolean activated = false;

    /**
     * Creates a new voltage monitoring trigger.
     * 
     * <p>
     * This trigger will be activated when the voltage is lower than the threshold,
     * but will remain activated as long as the voltage stays within the buffer zone
     * of the threshold (default 0.15V). This prevents the trigger from being
     * repeatedly activated due to noise and small fluctuations in the voltage.
     * </p>
     * 
     * @param threshold The voltage threshold
     */
    public VoltageMonitoringTrigger(double threshold) {
        this(threshold, 0.15);
    }

    /**
     * Creates a new voltage monitoring trigger.
     * 
     * <p>
     * This trigger will be activated when the voltage is lower than the threshold,
     * but will remain activated as long as the voltage stays within the buffer zone
     * of the threshold (default 0.15V). This prevents the trigger from being
     * repeatedly activated due to noise and small fluctuations in the voltage.
     * </p>
     * 
     * @param threshold The voltage threshold
     * @param buffer    The size of the buffer zone
     */
    public VoltageMonitoringTrigger(double threshold, double buffer) {
        this.threshold = threshold;
        this.buffer = buffer;
    }

    @Override
    public boolean get() {
        // A buffer to prevent the trigger from getting rapidly activated and
        // un-activated due to noise
        double voltage = RobotController.getBatteryVoltage();
        // If not yet activated, return whether the voltage is lower than the threshold
        if (!activated) {
            if (voltage < threshold) {
                activated = true;
                return true;
            } else {
                return false;
            }
        }
        // If activated, return true even for voltages higher than the threshold, as
        // long as it is within the buffer
        else {
            if (voltage >= threshold + buffer) {
                activated = false;
                return false;
            } else {
                return true;
            }
        }
    }
}
