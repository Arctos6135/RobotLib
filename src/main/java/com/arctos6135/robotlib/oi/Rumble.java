package com.arctos6135.robotlib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.lang.Thread;

/**
 * This class (and its only function) rumbles the controller anyway you like it.
 * 
 * <p>
 * Use cases include notifying the driver of certain values. It sets the rumble
 * to the intensity you want, and then waits a certain time before turning it
 * off. To run this function in another part of the project, and have it done
 * asynchronously, do it like this:
 * </p>
 * 
 * <pre>
 * Rumble myRumble = new Rumble(values); // set preset Rumbles that you will be using beforehand with their values ;
 *                                       // only needed once
 * myRumble.execute(); // whenever you need to rumble the controller, run this, using the Rumble
 *                     // object you created before
 * </pre>
 * 
 * @author Moeez Muhammad
 */
public class Rumble implements Runnable {

    private final int sleepTime;
    private final int repeatCount;
    private final GenericHID controller;
    private final String side;
    private final double intensity;

    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Rumble the left side of the controller.
     */
    public static final String SIDE_LEFT = "left";
    /**
     * Rumble the right side of the controller.
     */
    public static final String SIDE_RIGHT = "right";
    /**
     * Rumble both sides of the controller.
     */
    public static final String SIDE_BOTH = "both";

    /**
     * Creates a new Rumble. It will last 300ms and will not repeat.
     * 
     * @param controller The GenericHID object that you're rumbling on
     * @param side       A string value that details the side to rumble: "left",
     *                   "right", or "both"
     * @param intensity  The intensity to rumble at between 0 and 1
     */
    public Rumble(GenericHID controller, String side, double intensity) {
        this.controller = controller;
        this.side = side;
        this.intensity = intensity;

        repeatCount = 1;
        sleepTime = 300;
    }

    /**
     * Creates a new Rumble. It will not repeat.
     * 
     * @param controller The GenericHID object that you're rumbling on
     * @param side       A string value that details the side to rumble: "left",
     *                   "right", or "both"
     * @param intensity  The intensity to rumble at between 0 and 1
     * @param duration   The duration, in milliseconds, to rumble for
     */
    public Rumble(GenericHID controller, String side, double intensity, int duration) {
        this.controller = controller;
        this.side = side;
        this.intensity = intensity;
        this.sleepTime = duration;

        repeatCount = 1;
    }

    /**
     * Creates a new Rumble.
     * 
     * @param controller  The GenericHID object that you're rumbling on
     * @param side        A string value that details the side to rumble: "left",
     *                    "right", or "both"
     * @param intensity   The intensity to rumble at between 0 and 1
     * @param duration    The duration, in milliseconds, to rumble for
     * @param repeatCount The number of times to repeat this rumble
     */
    public Rumble(GenericHID controller, String side, double intensity, int duration, int repeatCount) {
        this.controller = controller;
        this.side = side;
        this.intensity = intensity;
        this.sleepTime = duration;
        this.repeatCount = repeatCount;
    }

    @Override
    public void run() {
        synchronized (this.controller) {
            try { // run() can't throw errors and Thread.sleep() can throw InterruptedException
                for (int i = 0; i < repeatCount; i++) {
                    if (side.equals(SIDE_RIGHT)) {
                        controller.setRumble(RumbleType.kRightRumble, intensity);
                        Thread.sleep(sleepTime);
                        controller.setRumble(RumbleType.kRightRumble, 0.0);
                    } else if (side.equals(SIDE_LEFT)) {
                        controller.setRumble(RumbleType.kLeftRumble, intensity);
                        Thread.sleep(sleepTime);
                        controller.setRumble(RumbleType.kLeftRumble, 0.0);
                    } else if (side.equals(SIDE_BOTH)) {
                        controller.setRumble(RumbleType.kRightRumble, intensity);
                        controller.setRumble(RumbleType.kLeftRumble, intensity);
                        Thread.sleep(sleepTime);
                        controller.setRumble(RumbleType.kRightRumble, 0.0);
                        controller.setRumble(RumbleType.kLeftRumble, 0.0);
                    }

                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                System.err.println("Rumble's sleep method returned an InterruptedException. Hope that never happens.");
            }
        }
    }

    /**
     * Executes the rumble. This runs asynchronously. 
     */
    public void execute() {
        executor.execute(this);
    }
}
