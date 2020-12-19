package edu.csc413.tankgame.model;

import edu.csc413.tankgame.GameDriver;
import edu.csc413.tankgame.view.RunGameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * GameState represents the state of the game "world." The GameState object tracks all of the moving entities like tanks
 * and shells, and provides the controller of the program (i.e. the GameDriver) access to whatever information it needs
 * to run the game. Essentially, GameState is the "data context" needed for the rest of the program.
 */
public class GameState {
    public static final double TANK_X_LOWER_BOUND = 30.0;
    public static final double TANK_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width - 100.0;
    public static final double TANK_Y_LOWER_BOUND = 30.0;
    public static final double TANK_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height - 120.0;

    public static final double SHELL_X_LOWER_BOUND = -10.0;
    public static final double SHELL_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width;
    public static final double SHELL_Y_LOWER_BOUND = -10.0;
    public static final double SHELL_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height;

    public static final String PLAYER_TANK_ID = "player-tank";
    public static final String AI_TANK_ID = "ai-tank";
    public static final String CUSHION_AI_TANK_ID = "cushion-ai-tank";
    public static final String BOMB_TANK_ID = "bomb-tank";
    public static final String LIFE_BAR_ID = "life-bar";
    public static final String POWER_UP_ID = "power-up";


    public ArrayList<GameEntity> collidedEntities = new ArrayList<GameEntity>();

    public ArrayList<GameEntity> lifeBoosts = new ArrayList<GameEntity>();

    public ArrayList<GameEntity> powerUp = new ArrayList<GameEntity>();





    // TODO: Feel free to add more tank IDs if you want to support multiple AI tanks! Just make sure they're unique.

    // TODO: Implement.
    // There's a lot of information the GameState will need to store to provide contextual information. Add whatever
    // instance variables, constructors, and methods are needed.

    private final ArrayList<GameEntity> entities = new ArrayList<>();

    private final ArrayList<Shell> shells = new ArrayList<>();

    private final ArrayList<Tank> enemyTanks = new ArrayList<>();

    public void addEntity(GameEntity entity){
        entities.add(entity);
    }

    public void addEnemyTank(Tank tank){
        entities.add(tank);
    }

    public void removeEntity(GameEntity entity) {
        entities.remove(entities.stream().filter(GameEntity -> GameEntity.getId().equals(entity.getId())).findAny().get());
    }

    public ArrayList<GameEntity> getEntities(){
        return entities;
    }

    public ArrayList<Tank> getEnemies(){
        return enemyTanks;
    }

    public ArrayList<GameEntity> getCollidedEntities(){
        return collidedEntities;
    }

    public ArrayList<Shell> getShells(){ return shells; }

    public void addShell(Shell shell){
        shells.add(shell);
    }

    public GameEntity getEntity(String id){
        return entities.stream().filter(GameEntity -> GameEntity.getId().equals(id)).findAny().get();
    }

    private boolean entitiesOverlap(GameEntity entity1, GameEntity entity2) {
        return  entity1.getX() < entity2.getXBound()
                && entity1.getXBound() > entity2.getX()
                && entity1.getY() < entity2.getYBound()
                && entity1.getYBound() > entity2.getY();
    }

    public void checkScreenBounds(GameEntity entity){
        if(entity instanceof Tank){
            if(entity.getX()>TANK_X_UPPER_BOUND ){
                entity.setX(TANK_X_UPPER_BOUND );
            }
            if(entity.getX()<TANK_X_LOWER_BOUND){
                entity.setX(TANK_X_LOWER_BOUND);
            }
            if(entity.getY()>TANK_Y_UPPER_BOUND ){
                entity.setY(TANK_Y_UPPER_BOUND);
            }
            if(entity.getY()<TANK_Y_LOWER_BOUND){
                entity.setY(TANK_Y_LOWER_BOUND);
            }
        }
        if(entity instanceof Shell||entity instanceof ExtraLifeBoost||entity instanceof PowerUp){
            if(entity.getX()>1024){
                entity.collisionTrue();
            }
            if(entity.getX()<0){
                entity.collisionTrue();
            }
            if(entity.getY()>768){
                entity.collisionTrue();
            }
            if(entity.getY()<0){
                entity.collisionTrue();
            }

        }

    }

    public void checkEntityBounds(GameEntity entity1, GameEntity entity2){
        if(entitiesOverlap(entity1, entity2)){
            if(entity1 instanceof BombTank && entity2 instanceof PlayerTank) {
                entity1.collisionTrue();
                entity2.collisionTrue();
            }
            if(entity1 instanceof PlayerTank && entity2 instanceof BombTank) {
                entity2.collisionTrue();
                entity1.collisionTrue();
            }
            else if(entity1 instanceof Tank && entity2 instanceof Tank) {
                double w = entity1.getXBound() - entity2.getX();
                double x = entity2.getXBound() - entity1.getX();
                double y = entity1.getYBound() - entity2.getY();
                double z = entity2.getYBound() - entity1.getY();
                double o = Math.min(w, Math.min(x, (Math.min(y, z))));
                if(o==w){
                    entity1.setX(entity1.getX()-(w/2));
                    entity2.setX(entity2.getX()+(w/2));
                }
                if(o==x){
                    entity1.setX(entity1.getX()+(x/2));
                    entity2.setX(entity1.getX()-(x/2));
                }
                if(o==y){
                    entity1.setY(entity1.getY()+(y/2));
                    entity2.setY(entity2.getY()-(y/2));
                }
                if(o==z){
                    entity2.setY(entity2.getY()+(z/2));
                    entity1.setY(entity2.getY()-(z/2));
                }
            }
            if(entity1 instanceof Tank && entity2 instanceof Shell){
                if(entity1.getId().equals(entity2.getOriginTank())){
// makes sure it doesn't immediately collide with the tank that shot it.
                }
                else{
                    entity1.collisionTrue();
                    entity2.collisionTrue();
                }
            }
            if(entity1 instanceof Shell && entity2 instanceof Tank){
                if(entity2.getId().equals(entity1.getOriginTank())){
// makes sure it doesn't immediately collide with the tank that shot it.
                }
                else {
                    entity1.collisionTrue();
                    entity2.collisionTrue();
                }

            }
            if(entity1 instanceof Shell && entity2 instanceof Wall){
                    entity1.collisionTrue();
                    entity2.collisionTrue();
            }
            if(entity1 instanceof Wall && entity2 instanceof Shell){
                    entity1.collisionTrue();
                    entity2.collisionTrue();
            }
            if(entity1 instanceof Wall && entity2 instanceof Tank){
                double w = entity1.getXBound() - entity2.getX();
                double x = entity2.getXBound() - entity1.getX();
                double y = entity1.getYBound() - entity2.getY();
                double z = entity2.getYBound() - entity1.getY();
                double o = Math.min(w, Math.min(x, (Math.min(y, z))));
                if(o==w){
                    entity2.setX(entity2.getX()-w);
                }
                if(o==x){
                    entity2.setX(entity2.getX()+x);
                }
                if(o==y){
                    entity2.setY(entity2.getY()+y);
                }
                if(o==z){
                    entity2.setY(entity2.getY()-z);
                }
            }
            if(entity1 instanceof Tank && entity2 instanceof Wall){
                double w = entity1.getXBound() - entity2.getX();
                double x = entity2.getXBound() - entity1.getX();
                double y = entity1.getYBound() - entity2.getY();
                double z = entity2.getYBound() - entity1.getY();
                double o = Math.min(w, Math.min(x, (Math.min(y, z))));
                if(o==w){
                    entity1.setX(entity1.getX()-w);
                }
                if(o==x){
                    entity1.setX(entity1.getX()+x);
                }
                if(o==y){
                    entity1.setY(entity1.getY()+y);
                }
                if(o==z){
                    entity1.setY(entity1.getY()-z);
                }
            }
            if(entity1 instanceof ExtraLifeBoost && entity2 instanceof PlayerTank){
                entity2.addLife();
                entity1.collisionTrue();
            }
            if(entity1 instanceof PlayerTank && entity2 instanceof ExtraLifeBoost){
                entity1.addLife();
                entity2.collisionTrue();
            }
            if(entity1 instanceof Wall && entity2 instanceof ExtraLifeBoost){
                lifeBoosts.remove(entity2);
            }
            if(entity1 instanceof ExtraLifeBoost && entity2 instanceof Shell){
                lifeBoosts.remove(entity1);
            }
            if(entity1 instanceof Wall && entity2 instanceof PowerUp){
                lifeBoosts.remove(entity2);
            }
            if(entity1 instanceof PowerUp && entity2 instanceof Wall){
                lifeBoosts.remove(entity1);
            }
            if(entity1 instanceof ExtraLifeBoost && entity2 instanceof Shell){
                lifeBoosts.remove(entity1);
            }
            if(entity1 instanceof PlayerTank && entity2 instanceof PowerUp){
                entity1.setCoolDownValue(20);
                entity1.resetPowerUpCounter();
                powerUp.remove(entity2);
                entity2.collisionTrue();
            }
            if(entity1 instanceof PowerUp && entity2 instanceof PlayerTank){
                entity2.setCoolDownValue(20);
                entity2.resetPowerUpCounter();
                powerUp.remove(entity1);
                entity1.collisionTrue();
            }
        }
    }

    public void checkCollisions() {
        for (GameEntity entity : entities) {
            if (entity.collision()) {
                if (entity instanceof Shell) {
                    collidedEntities.add(entity);
                }
                if (entity instanceof Tank) {
                    if ((entity).getLives() == 0) {
                        collidedEntities.add(entity);
                    } else {
                        (entity).decrementLives();
                        entity.collisionFalse();
                    }
                }
                if(entity instanceof CushionTank||entity instanceof BombTank){
                    enemyTanks.remove(entity);
                }
                if(entity instanceof Wall){
                    if ((entity).getLives() == 0) {
                        collidedEntities.add(entity);
                    } else {
                        (entity).decrementLives();
                        entity.collisionFalse();
                    }
                }
                if(entity instanceof ExtraLifeBoost){
                    lifeBoosts.remove(entity);
                    collidedEntities.add(entity);
                }
                if(entity instanceof PowerUp){
                    collidedEntities.add(entity);
                }
                if(entity instanceof BombTank){
                    entity.decrementLives();
                    collidedEntities.add(entity);
                }
            }
        }
    }

    public int randomNumber(int max, int min){
        return (int)(Math.random()*((max-min)+1))+min;
    }

    public double randomX(){
        double x = (Math.random()*((1024-1)+1))+1;
        return x;
    }

    public double randomY(){
        double y= (Math.random()*((768-1)+1))+1;
        return y;
    }
// adds an extra life based on generated number.
    public void addExtraLife() {
        int x = randomNumber(400, 1);
        if ((x == 7) && (lifeBoosts.size()<2)) {
            ExtraLifeBoost extraLifeBoost = new ExtraLifeBoost("extra-life", randomX(), randomY(), 0);
            entities.add(extraLifeBoost);
           lifeBoosts.add(extraLifeBoost);
        }
    }
// adds a power up randomly. Checks that there are no existing power ups on screen and that the player
    // tank does not actively hold one.
    public void addPowerUp() {
        int x = randomNumber(600, 1);
        if(getEntity(PLAYER_TANK_ID).getPowerUpCounter()==0&&(x==10)&&powerUp.isEmpty()) {
            PowerUp powerup = new PowerUp(POWER_UP_ID,randomX(),randomY(),0);
            entities.add(powerup);
            powerUp.add(powerup);
        }
    }

    }
