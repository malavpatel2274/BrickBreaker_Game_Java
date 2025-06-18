/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author malav
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int[] map1 : map) {
            for (int j = 0; j < map[0].length; j++) {
                map1[j] = 1;
            }
        }

        brickWidth = 540 / col;
        brickHeight = 150 / row;

        // Set random colors for each brick when the game starts
        setRandomColors();
    }

    private void setRandomColors() {
        for (int[] map1 : map) {
            for (int j = 0; j < map[0].length; j++) {
                // Assign a random color to each brick
                map1[j] = getRandomColor();
            }
        }
    }

    private int getRandomColor() {
        // You can customize this method to generate random colors based on your requirements
        Random random = new Random();
        return random.nextInt(16777215); // Generating a random color as an integer
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Use the randomly generated color for each brick
                    g.setColor(new Color(map[i][j]));

                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    // this is just to show separate brick, game can still run without it
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}
