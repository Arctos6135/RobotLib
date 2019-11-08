package com.arctos6135.robotlib.oi.buttons;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The {@code AnyOfButton} class is a wrapper around a normal {@link Button}
 * that activates when any one of multiple buttons are activated.
 * 
 * @author Tyler Tian
 */
public class AnyOfButton extends Button {

    private Button[] buttons;

    /**
     * Creates a new {@link AnyOfButton}.
     * 
     * @param buttons The buttons that could be pressed
     */
    public AnyOfButton(Button... buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean get() {
        for (Button b : buttons) {
            // If any button is pressed, this button activates
            if (b.get()) {
                return true;
            }
        }
        return false;
    }
}
