package ca.cmpt213.CatAndMouse.UI;

import ca.cmpt213.CatAndMouse.Logic.Maze;
import ca.cmpt213.CatAndMouse.Logic.MazeModListener;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to control all the GUI related tasks (Updating UI, playing sounds, showing alert dialogues).
 */
public class MazeGUI implements MazeModListener{

    public static final int INVALID_MOVE = 1;
    public static final int GAME_LOST = 2;
    public static final int GAME_WON = 3;
    public static final int CHEESE_COLLECTED = 4;
    public static final int PLAYER_MOVE = 5;
    public static final int CAT_MOVE = 6;
    public static final int MAZE_REVEAL = 7;

    private JPanel gameGrid;
    private JPanel statsPanel;
    private static JFrame parentFrame;
    private Maze gameMaze;

    private static final String playerIMG = "src/sprites/sp_player_right.jpg";
    private static final String deadIMG = "src/sprites/sp_dead.jpg";
    private final String wallIMG = "src/sprites/sp_wall.jpg";
    private final String catRedIMG = "src/sprites/sp_red.jpg";
    private final String cheeseIMG = "src/sprites/sp_cheese.jpg";
    private final String fogIMG = "src/sprites/sp_fog.jpg";
    private final String revealIMG = "src/sprites/sp_reveal.jpg";

    private ImageIcon wall;
    private ImageIcon player;
    private ImageIcon catRed;
    private ImageIcon cheese;
    private ImageIcon fog;
    private ImageIcon reveal;
    private ImageIcon dead;


    public MazeGUI(JFrame gameFrame, Maze gameMaze) {
        gameGrid = new JPanel(new GridBagLayout());

        statsPanel = new JPanel(new BorderLayout(0,0));

        parentFrame = gameFrame;

        parentFrame.add(gameGrid, BorderLayout.CENTER);
        parentFrame.add(statsPanel, BorderLayout.SOUTH);

        this.gameMaze = gameMaze;

        wall = new ImageIcon(wallIMG);
        player = new ImageIcon(playerIMG);
        cheese = new ImageIcon(cheeseIMG);
        catRed = new ImageIcon(catRedIMG);
        fog = new ImageIcon(fogIMG);
        dead = new ImageIcon(deadIMG);
        reveal = new ImageIcon(revealIMG);

        updateGameGrid();
        statsModified();
    }

    public void updateGameGrid() {
        ArrayList<Character> mazeView = gameMaze.getMazeView();

        gameGrid.removeAll();

        calPlayerOrientation();

        for(int x = 0; x < mazeView.size(); x++) {
            Character mazeChar = mazeView.get(x);

            HashMap<Character, Integer> tempHashMap = calculateGridPos(x);

            GridBagConstraints c = new GridBagConstraints();

            c.gridx = tempHashMap.get('x');
            c.gridy = tempHashMap.get('y');

            switch(mazeChar) {
                case '#':
                    gameGrid.add(new JLabel(wall), c);
                    break;
                case '@':
                    gameGrid.add(new JLabel(player), c);
                    break;
                case '$':
                    gameGrid.add(new JLabel(cheese), c);
                    break;
                case '!':
                    gameGrid.add(new JLabel(catRed), c);
                    break;
                case 'X':
                    gameGrid.add(new JLabel(dead), c);
                    break;
                case ' ':
                    gameGrid.add(new JLabel(reveal), c);
                    break;
                case '.':
                    gameGrid.add(new JLabel(fog), c);
                    break;
            }
        }

        gameGrid.revalidate();

        parentFrame.repaint();
    }

    @Override
    public void mazeModified() {
        updateGameGrid();
    }

    @Override
    public void statsModified() {
        statsPanel.removeAll();

        JLabel stats = new JLabel("Cheese collected: " + gameMaze.getNumCheeseCollected() + " out of 5.");
        stats.setForeground(Color.WHITE);
        statsPanel.add(stats, BorderLayout.EAST);
        statsPanel.setBackground(Color.BLACK);
    }

    public static void infoBox(String infoMessage, String titleBar, int iconCode) {

        ImageIcon icon = null;

        if(iconCode == GAME_WON) {
            icon = new ImageIcon(playerIMG);
        }

        if(iconCode == GAME_LOST) {
            icon = new ImageIcon(deadIMG);
        }

        JOptionPane.showMessageDialog(parentFrame,infoMessage,titleBar,JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public static void playSound(int context) {

        File sound;

        switch (context) {
            case INVALID_MOVE:
                sound = new File("src/sounds/invalid_move.wav");
                break;
            case GAME_LOST:
                sound = new File("src/sounds/game_lost.wav");
                break;
            case GAME_WON:
                sound = new File("src/sounds/game_won.wav");
                break;
            case CHEESE_COLLECTED:
                sound = new File("src/sounds/cheese_collected.wav");
                break;
            case PLAYER_MOVE:
                sound = new File("src/sounds/player_move.wav");
                break;
            case CAT_MOVE:
                sound = new File("src/sounds/cat_move.wav");
                break;
            case MAZE_REVEAL:
                sound = new File("src/sounds/maze_reveal.wav");
                break;
            default:
                sound = new File("");
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
            Clip clip = AudioSystem.getClip(); clip.open(audioInputStream); clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Character, Integer> calculateGridPos(int arrayIndex) {
        final int width = gameMaze.getMazeWidth();

        HashMap<Character, Integer> xyPos = new HashMap<>(2);

        int y = (int)arrayIndex / width;
        int x = arrayIndex - (width * y);

        xyPos.put('x', x);
        xyPos.put('y', y);

        return xyPos;
    }

    private void calPlayerOrientation() {
        if(!(MazeInputController.lastInput.equals("M") || MazeInputController.lastInput.equals("E"))
                || MazeInputController.lastInput.equals("H")) {
            String dir = "src/sprites/sp_player_";
            player = new ImageIcon(dir + MazeInputController.lastInput.toLowerCase() + ".jpg");
        }
    }
}
