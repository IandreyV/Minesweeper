package minesweeper.game;

import minesweeper.gui.MinesFieldFrame;
import javax.swing.*;
import java.awt.EventQueue;

/**
    Class is intended to run game.
*/
public class GameTest {

    public static void main(String... args) {
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new MinesFieldFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}