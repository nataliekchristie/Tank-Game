package edu.csc413.tankgame.view;

import edu.csc413.tankgame.GameDriver;
import edu.csc413.tankgame.model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

/**
 * MainView is the primary view that contains and controls individual screens (represented by the separate StartMenuView
 * and RunGameView classes). MainView can be interacted with to set which of those screens is currently showing, and it
 * is also registered to listen for keyboard events.
 */
public class MainView {
    /** The different screens that can be shown. */
    public enum Screen {
        START_MENU_SCREEN("start"),
        RUN_GAME_SCREEN("game"),
        END_MENU_SCREEN("end"),
        PAUSE_MENU_SCREEN("pause");

        private final String screenName;

        Screen(String screenName) {
            this.screenName = screenName;
        }

        public String getScreenName() {
            return screenName;
        }
    }

    private final JFrame mainJFrame;
    private final JPanel mainPanel;
    private final CardLayout mainPanelLayout;
    private final RunGameView runGameView;

    private String direction = "";
    public String getDirection(){
        return direction;
    }
    public void setDirection(String direction){
        this.direction = direction;
    }

    private String action ="";

    // TODO: Finish implementing this.
    // MainView is responsible for assigning listeners to various UI components (like buttons and keyboard input).
    // However, we want to return control to GameDriver when those events happen. How can we have listeners that directs
    // us back to the code in GameDriver?
    public MainView() {
        mainJFrame = new JFrame();
        mainJFrame.setVisible(false);
        mainJFrame.setResizable(false);
        mainJFrame.setTitle("Tank Wars");
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.addKeyListener(new tankController());
        mainPanel = new JPanel();
        mainPanelLayout = new CardLayout();
        mainPanel.setLayout(mainPanelLayout);
        mainJFrame.setFocusable(true);

        StartMenuView startMenuView = new StartMenuView("Start Game");
        mainPanel.add(startMenuView, Screen.START_MENU_SCREEN.getScreenName());

        StartMenuView endMenuView = new StartMenuView("Restart Game");
        mainPanel.add(endMenuView, Screen.END_MENU_SCREEN.getScreenName());

        StartMenuView pauseMenuView = new StartMenuView("Continue Game");
        mainPanel.add(pauseMenuView, Screen.PAUSE_MENU_SCREEN.getScreenName());

        runGameView = new RunGameView();
        mainPanel.add(runGameView, Screen.RUN_GAME_SCREEN.getScreenName());

        mainJFrame.add(mainPanel);
    }

    public void setAction(String action){
        this.action = action;
    }

    public String getAction(){
        return action;
    }
    /**
     * Returns the contained RunGameView. This method provides the GameDriver with direct access, which is needed for
     * updating game-related graphics while the program is running.
     */
    public RunGameView getRunGameView() {
        return runGameView;
    }

    /** Changes the screen that is currently showing. */
    public void setScreen(Screen screen) {
        mainJFrame.setVisible(false);
        Dimension screenSize = switch (screen) {
            case START_MENU_SCREEN, END_MENU_SCREEN, PAUSE_MENU_SCREEN -> StartMenuView.SCREEN_DIMENSIONS;
            case RUN_GAME_SCREEN -> RunGameView.SCREEN_DIMENSIONS;
           // case PAUSE_MENU_SCREEN -> StartMenuView.SCREEN_DIMENSIONS;
        };
        mainJFrame.setSize(screenSize);
        mainPanelLayout.show(mainPanel, screen.getScreenName());

        mainJFrame.setVisible(true);
    }

    /** Ends the program. */
    public void closeGame() {
        mainJFrame.dispatchEvent(new WindowEvent(mainJFrame, WindowEvent.WINDOW_CLOSING));
    }

    public class tankController implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

            int keyType = e.getKeyCode();
            if(keyType==KeyEvent.VK_UP) {
                setDirection("up");
            }
            if(keyType==KeyEvent.VK_DOWN) {
                setDirection("down");
            }
            if (keyType==KeyEvent.VK_RIGHT) {
                setDirection("right");
            }
            if(keyType==KeyEvent.VK_LEFT) {
                setDirection("left");
            }
            if(keyType==KeyEvent.VK_SPACE) {
                setDirection("shoot");
            }
            if(keyType==KeyEvent.VK_ESCAPE) {
                setDirection("esc");
            }
            if(keyType==KeyEvent.VK_ENTER) {
                setDirection("pause");
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
