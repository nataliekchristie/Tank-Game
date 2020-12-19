package edu.csc413.tankgame;

import com.sun.tools.javac.Main;
import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;

import java.util.List;


import edu.csc413.tankgame.WallImageInfo;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver {
    /*

    Added pause menu (press enter), animations, health bar, game over.

    =========> Press Enter to pause.
    =========> Yellow hearts represent new lives that can be picked up
    ========> Boost item allows you to shoot faster for a short period of time
    ========> Health bar displayed top left corner.
    ============> "GAME OVER" screen stalls before returning to main menu.
    ============> Animations appropriately effect walls, shells, and tanks, but not upgrades.



     */
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;
    private boolean esc = false;
    private double stall = 500;
    private double countdown = 300;


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
       /*
       Enemy tanks are interchangeable. The regular "ai" tank is more of a dumb tank that points and shoots.
       The cushion tank is the one you gave to us in class, and the bomb tank is a tank that will
       try to kill itself by colliding with you. It follows you around the screen until it collides.
        */
        BombTank bombTank = new BombTank(
                gameState.BOMB_TANK_ID,
                runGameView.AI_TANK_INITIAL_X,
                runGameView.AI_TANK_INITIAL_Y,
                runGameView.AI_TANK_INITIAL_ANGLE
        );
       CushionTank cushionTank = new CushionTank(
                gameState.CUSHION_AI_TANK_ID,
                runGameView.AI_TANK_2_INITIAL_X,
                runGameView.AI_TANK_2_INITIAL_Y,
                runGameView.AI_TANK_2_INITIAL_ANGLE
        );
        ////// CREATE WALLS, ASSIGN IDs
        List<WallImageInfo> wallImages = WallImageInfo.readWalls();
        for(WallImageInfo wall: wallImages){
            Wall newWall = new Wall(wall.getX(),wall.getY(),0,wall.getImageFile());
            gameState.addEntity(newWall);
        }
        //////
        gameState.addEntity(playerTank);
        // Change to whatever enemy tanks.
        gameState.addEntity(bombTank);
        gameState.addEntity(cushionTank);
        //keeping track of status of enemy tanks
        gameState.addEnemyTank(bombTank);
        gameState.addEnemyTank(cushionTank);
        // Remember to update tank choices here as well.
        runGameView.addDrawableEntity(gameState.PLAYER_TANK_ID,runGameView.PLAYER_TANK_IMAGE_FILE,runGameView.PLAYER_TANK_INITIAL_X,
                runGameView.PLAYER_TANK_INITIAL_Y,runGameView.PLAYER_TANK_INITIAL_ANGLE);
        runGameView.addDrawableEntity(gameState.BOMB_TANK_ID,runGameView.AI_TANK_IMAGE_FILE,runGameView.AI_TANK_INITIAL_X,
                runGameView.AI_TANK_INITIAL_Y,runGameView.AI_TANK_INITIAL_ANGLE);
        runGameView.addDrawableEntity(gameState.CUSHION_AI_TANK_ID,runGameView.AI_TANK_IMAGE_FILE,runGameView.AI_TANK_2_INITIAL_X,
                runGameView.AI_TANK_2_INITIAL_Y,runGameView.AI_TANK_2_INITIAL_ANGLE);
        /// Paint walls
        for(GameEntity entity: gameState.getEntities()){
            if(entity instanceof Wall){
                    runGameView.addDrawableEntity(entity.getId(),((Wall) entity).getImageFile(),entity.getX(),entity.getY(),entity.getAngle());
                }
            }

        runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_FIVE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);

        ///

        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }

            while(stall>0) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
                stall -= 1;
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
        checkViewKeys();
        //move
        for(GameEntity entity: gameState.getEntities()){
            entity.move(gameState);
        }

        for(Shell shell: gameState.getShells()){
            gameState.addEntity(shell);
        }

        gameState.getShells().clear(); ///<== what is this lol
        //check bounds (for tanks)

        //check bounds, erase shells (work)
        for(GameEntity entity1: gameState.getEntities()){
            for(GameEntity entity2: gameState.getEntities()){
                gameState.checkEntityBounds(entity1, entity2);
            }
        }

        for(GameEntity entity1: gameState.getEntities()){
                gameState.checkScreenBounds(entity1);
        }

        //check collisions (work)
        gameState.checkCollisions();


        //generate extra life boost
        gameState.addExtraLife();


        gameState.addPowerUp();
        //add power-up if ready


        //check if there is new shell to draw
        for(GameEntity entity: gameState.getEntities()){
            if(entity instanceof Shell){
                if(entity.isNew()){
                    runGameView.addDrawableEntity(entity.getId(),runGameView.SHELL_IMAGE_FILE,entity.getX(),entity.getY(),entity.getAngle());
                    entity.makeOld();
                }
            }
            // checks if there's new life boosts
            if(entity instanceof ExtraLifeBoost){
                if(entity.isNew()){
                    runGameView.addDrawableEntity(entity.getId(),runGameView.EXTRA_LIFE_IMG,entity.getX(),entity.getY(),entity.getAngle());
                    entity.makeOld();
                }
            }

            if(entity instanceof PowerUp){
                runGameView.addDrawableEntity(entity.getId(),runGameView.POWER_UP_IMG,entity.getX(),entity.getY(),
                        entity.getAngle());
            }
        }
        //remove drawable entity for colliding shells
        // runs animation for shells

        for(GameEntity entity: gameState.getCollidedEntities()) {
            gameState.getEntities().remove(entity);
            runGameView.removeDrawableEntity(entity.getId());
            if (entity instanceof Shell || entity instanceof Wall) {
                runGameView.addAnimation(runGameView.SHELL_EXPLOSION_ANIMATION, runGameView.SHELL_EXPLOSION_FRAME_DELAY,
                        entity.getX(), entity.getY());
            } else if (entity instanceof Tank) {
                runGameView.addAnimation(runGameView.BIG_EXPLOSION_ANIMATION, runGameView.BIG_EXPLOSION_FRAME_DELAY,
                        entity.getX(), entity.getY());
                if (entity.getId().equals(gameState.PLAYER_TANK_ID)||gameState.getEnemies().isEmpty()) {
                  runGameView.addDrawableEntity("GAMEOVER",runGameView.GAME_OVER_IMG,
                            runGameView.GAME_OVER_X,runGameView.GAME_OVER_Y,runGameView.GAME_OVER_ANGLE);
                    return false;
                }
            }
        }

        //remove collided entities so they do not register through the next update.
        gameState.getCollidedEntities().clear();

        //update life-bar
        checkLifeBar();

        double lives = gameState.getEntity(gameState.PLAYER_TANK_ID).getLives();
        if(lives==4) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_FIVE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==3) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_FOUR_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==2) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_THREE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==1) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_TWO_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        if(lives==0) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_ONE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        if(lives<0) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_ZERO_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        //draw
        for(GameEntity entity: gameState.getEntities()){
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(),entity.getX(),entity.getY(),entity.getAngle());
        }
        // check cooldown for tanks
        for(GameEntity entity: gameState.getEntities()) {
            if (entity instanceof Tank) {
                if (entity.getCoolDown() > 0) {
                    entity.decrementCoolDown();
                }
            }

        }

        //checks if there's an active powerup
        if(gameState.getEntity(gameState.PLAYER_TANK_ID).getPowerUpCounter()>0){
            gameState.getEntity(gameState.PLAYER_TANK_ID).decrementPowerUp();
        }


        // Resets so the player tank can continue to respond to key input
        gameState.getEntity(gameState.PLAYER_TANK_ID).switchOff();
        // ends if escape key is pressed
        if(esc){
            return false;
        }
        return true;
    }

    public void checkViewKeys(){
        String direction = mainView.getDirection();
        if(direction.equals("up")){
            gameState.getEntity(gameState.PLAYER_TANK_ID).switchUpOn();
        }
        else if(direction.equals("down")){
            gameState.getEntity(gameState.PLAYER_TANK_ID).switchDownOn();
        }
        else if(direction.equals("left")){
            gameState.getEntity(gameState.PLAYER_TANK_ID).switchLeftOn();
        }
        else if(direction.equals("right")){
            gameState.getEntity(gameState.PLAYER_TANK_ID).switchRightOn();
        }
        else if(direction.equals("shoot")){
            gameState.getEntity(gameState.PLAYER_TANK_ID).shoot(gameState);
        }
        else if(direction.equals("esc")){
            esc = true;
        }
        else if(direction.equals("pause")){
            mainView.setScreen(MainView.Screen.PAUSE_MENU_SCREEN);
        }
        mainView.setDirection("");
    }

    public void checkLifeBar(){

        double lives = gameState.getEntity(gameState.PLAYER_TANK_ID).getLives();
        if(lives==4) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_FIVE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==3) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_FOUR_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==2) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_THREE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        else if(lives==1) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_TWO_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        if(lives==0) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_ONE_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
        if(lives<0) {
            runGameView.addDrawableEntity(gameState.LIFE_BAR_ID,runGameView.LIFE_BAR_ZERO_LIVES,runGameView.LIFE_BAR_INITIAL_X,
                    runGameView.LIFE_BAR_INITIAL_Y, runGameView.LIFE_BAR_INITIAL_ANGLE);
        }
    }


    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }

}
