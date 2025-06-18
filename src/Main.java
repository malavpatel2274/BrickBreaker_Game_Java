/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author malav
 */

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame obj = new JFrame("Breakout Ball");
        Gameplay gamePlay = new Gameplay();

        obj.setBounds(10, 10, 700, 600);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.setResizable(false);
        obj.add(gamePlay);
        
        // Ensure the game starts with focus
        //obj.setFocusable(true);
        obj.requestFocusInWindow();
        obj.setVisible(true);
    }
}
