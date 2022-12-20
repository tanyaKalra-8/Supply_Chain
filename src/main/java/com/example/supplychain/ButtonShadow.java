package com.example.supplychain;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;

public class ButtonShadow {

    public static void draw(Button button) {
        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        button.setEffect(shadow);
                    }
                });
        //Removing the shadow when the mouse cursor is off
        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        button.setEffect(null);
                    }
                });
    }
}
