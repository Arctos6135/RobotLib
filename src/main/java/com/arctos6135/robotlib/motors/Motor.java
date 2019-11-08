package com.arctos6135.robotlib.motors;

/**
 * The {@code Motor} interface is an interface that represents any kind of motor
 * that can be set to an output value.
 * 
 * @author Tyler Tian
 */
@FunctionalInterface
public interface Motor {
    /**
     * Sets the motor to the value given. This value is typically the percentage
     * output, but doesn't have to.
     * 
     * @param value The value to set the motor to
     */
    public void set(double value);
}
