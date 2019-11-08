package com.arctos6135.robotlib.oi;

/**
 * This class provides a mapping of the buttons, axis and POV of the XBox
 * Controller.
 * 
 * @author Tyler Tian
 */
public final class XboxControllerButtons {
    /**
     * The x-axis (horizontal axis) of the left analog stick.
     */
    public static final int LSTICK_X_AXIS = 0;
    /**
     * The y-axis (vertical axis) of the left analog stick.
     */
    public static final int LSTICK_Y_AXIS = 1;
    /**
     * The x-axis (horizontal axis) of the right analog stick.
     */
    public static final int RSTICK_X_AXIS = 4;
    /**
     * The y-axis (vertical axis) of the right analog stick.
     */
    public static final int RSTICK_Y_AXIS = 5;
    /**
     * The left analog trigger.
     */
    public static final int LTRIGGER = 2;
    /**
     * The right analog trigger.
     */
    public static final int RTRIGGER = 3;

    /**
     * The A button.
     */
    public static final int BUTTON_A = 1;
    /**
     * The B button.
     */
    public static final int BUTTON_B = 2;
    /**
     * The X button.
     */
    public static final int BUTTON_X = 3;
    /**
     * The Y button.
     */
    public static final int BUTTON_Y = 4;
    /**
     * The left bumper.
     */
    public static final int LBUMPER = 5;
    /**
     * The right bumper.
     */
    public static final int RBUMPER = 6;
    /**
     * The back button.
     */
    public static final int BUTTON_BACK = 7;
    /**
     * The start button.
     */
    public static final int BUTTON_START = 8;
    /**
     * The button under the left analog stick.
     */
    public static final int BUTTON_LSTICK = 9;
    /**
     * The button under the right analog stick.
     */
    public static final int BUTTON_RSTICK = 10;

    /**
     * Up on the POV pad.
     */
    public static final int POV_UP = 0;
    /**
     * Upper right on the POV pad.
     */
    public static final int POV_UPPER_RIGHT = 45;
    /**
     * Right on the POV pad.
     */
    public static final int POV_RIGHT = 90;
    /**
     * Lower right on the POV pad.
     */
    public static final int POV_LOWER_RIGHT = 135;
    /**
     * Down on the POV pad.
     */
    public static final int POV_DOWN = 180;
    /**
     * Lower left on the POV pad.
     */
    public static final int POV_LOWER_LEFT = 225;
    /**
     * Left on the POV pad.
     */
    public static final int POV_LEFT = 270;
    /**
     * Upper left on the POV pad.
     */
    public static final int POV_UPPER_LEFT = 315;
    /**
     * Center (no movement) on the POV pad.
     */
    public static final int POV_CENTER = -1;
}
