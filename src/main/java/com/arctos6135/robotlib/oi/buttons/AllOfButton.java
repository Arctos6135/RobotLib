package com.arctos6135.robotlib.oi.buttons;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The {@code AllOfButton} class is a wrapper around a normal {@link Button}
 * that activates when all of multiple buttons are activated at the same time.
 * 
 * @author Tyler Tian
 */
public class AllOfButton extends Button {

    private Button[] buttons;

    /**
     * Creates a new {@link AllOfButton}.
     * 
     * @param buttons The buttons that need to be pressed together
     */
    public AllOfButton(Button... buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean get() {
        for (Button b : buttons) {
            // If any button is not activated, this button is not activated
            if (!b.get()) {
                return false;
            }
        }
        return true;
    }
}
