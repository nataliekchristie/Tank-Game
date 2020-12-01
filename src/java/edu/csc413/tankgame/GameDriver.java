package edu.csc413.tankgame;

import com.sun.tools.javac.Main;
import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver implements KeyListener {
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;

    public GameDriver() {
        mainView = new MainView();
        runGameView = mainView.getRunGameView();
        gameState = new GameState();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);
        // This should set the MainView's screen to the start menu screen.
    }

    public void runView(){
        runGameView.addKeyListener(key);
        runGameView.setFocusable(true);
        runGameView.requestFocus();
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        runGame();
    }

    public void runExit(){
        mainView.closeGame();
    }

    private void runGame() {
        PlayerTank playerTank = new PlayerTank(
                gameState.PLAYER_TANK_ID,
                runGameView.PLAYER_TANK_INITIAL_X,
                runGameView.PLAYER_TANK_INITIAL_Y,
                runGameView.PLAYER_TANK_INITIAL_ANGLE
        );
        AiTank aiTank = new AiTank(
                gameState.AI_TANK_ID,
                runGameView.AI_TANK_INITIAL_X,
                runGameView.AI_TANK_INITIAL_Y,
                runGameView.AI_TANK_INITIAL_ANGLE
        );
        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);
        runGameView.addDrawableEntity(gameState.PLAYER_TANK_ID,runGameView.PLAYER_TANK_IMAGE_FILE,runGameView.PLAYER_TANK_INITIAL_X,
                runGameView.PLAYER_TANK_INITIAL_Y,runGameView.PLAYER_TANK_INITIAL_ANGLE);
        runGameView.addDrawableEntity(gameState.AI_TANK_ID,runGameView.AI_TANK_IMAGE_FILE,runGameView.AI_TANK_INITIAL_X,
                runGameView.AI_TANK_INITIAL_Y,runGameView.AI_TANK_INITIAL_ANGLE);
        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            /*
            when game ends and update quits, end screen should start
             */
        };
        new Thread(gameRunner).start();
    }



        // TODO: Implement.
    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        //move
        for(GameEntity entity: gameState.getEntities()){
            entity.move(gameState);
        }
        //check bounds (for tanks)
            if(gameState.getEntity("player-tank").getX()>1024){
                  gameState.getEntity("player-tank").setX(1024);
            }
            else if(gameState.getEntity("player-tank").getX()<0){
                gameState.getEntity("player-tank").setX(0);
            }
            if(gameState.getEntity("player-tank").getY()>768){
                gameState.getEntity("player-tank").setY(768);
            }
            else if(gameState.getEntity("player-tank").getY()<0){
                gameState.getEntity("player-tank").setY(0);
            }
        if(gameState.getEntity("ai-tank").getX()>1024){
            gameState.getEntity("ai-tank").setX(1024);
        }
        else if(gameState.getEntity("ai-tank").getX()<0){
            gameState.getEntity("ai-tank").setX(0);
        }
        if(gameState.getEntity("ai-tank").getY()>768){
            gameState.getEntity("ai-tank").setY(768);
        }
        else if(gameState.getEntity("ai-tank").getY()<0){
            gameState.getEntity("ai-tank").setY(0);
        }
        //check bounds, erase shells (work)
        //check collisions (work)
        //draw
        for(GameEntity entity: gameState.getEntities()){
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(),entity.getX(),entity.getY(),entity.getAngle());
        }
        gameState.getEntity("player-tank").switchOff();
        // Resets so the player tank can continue to respond to key input
        return true;
    }

    KeyListener key = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

            int keyType = e.getKeyCode();
            if(keyType==KeyEvent.VK_UP) {
                gameState.getEntity(gameState.PLAYER_TANK_ID).switchUpOn();
            }
            if(keyType==KeyEvent.VK_DOWN) {
                gameState.getEntity(gameState.PLAYER_TANK_ID).switchDownOn();
            }
            if (keyType==KeyEvent.VK_RIGHT) {
                gameState.getEntity(gameState.PLAYER_TANK_ID).switchRightOn();
            }
            if(keyType==KeyEvent.VK_LEFT) {
                gameState.getEntity(gameState.PLAYER_TANK_ID).switchLeftOn();
            }
            if(keyType==KeyEvent.VK_SPACE){
                GameEntity entity = gameState.getEntity(gameState.PLAYER_TANK_ID);
                Shell newShell = new Shell(entity.getX(),entity.getY(),entity.getAngle());
                newShell.switchUpOn();
                gameState.addEntity(newShell);
                /*
                I know this isn't completely accurate but I essentially am trying to create
                a shell at the location that the tank currently is, add it to the
                list with the move function on, so that way when it is commanded to move
                it will do so and be able to be drawn.
                 */
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }

}
