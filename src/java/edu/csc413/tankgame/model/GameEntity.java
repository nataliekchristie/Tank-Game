package edu.csc413.tankgame.model;

import java.util.ArrayList;

public class GameEntity {
    private String id;
    private double x;
    private double y;
    private double angle;
    private double speed;
    private boolean up,down,right,left = false;
    private double turnSpeed;
    private double coolDown = 300;
    private boolean isNew = true;
    private boolean collided = false;
    private double lives = 3;
    private String originTank;
    private double powerUpCounter = 0;

    private double Xsize = 0;
    private double Ysize = 0;

    public GameEntity(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public GameEntity(String id, double x, double y, double angle){
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getLives(){
        return lives;
    }

    public void decrementLives(){
        lives-=1;
    }

    public void setLives(double lives){
        this.lives = lives;
    }

    public void addLife(){ lives+=1; }

    public double getCoolDown(){
        return coolDown;
    }

    public void decrementCoolDown(){
        coolDown-=1;
    }

    public void resetCoolDown(){
        coolDown = 300;
    }

    public void setCoolDownValue(double x){
        coolDown = x;
    }

    public void collisionTrue(){
        collided = true;
    }

    public void collisionFalse(){
        collided = false;
    }

    public boolean collision(){
        return collided;
    }

    public String getId(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return speed;
    }

    public void setTurnSpeed(double turnSpeed){
        this.turnSpeed = turnSpeed;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getAngle(){
        return angle;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

    protected void moveForward() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
    }


    protected void moveBackward() {
        double x = getX();
        double y = getY();
        x -= getSpeed() * Math.cos(getAngle());
        y -= getSpeed() * Math.sin(getAngle());
        setX(x);
        setY(y);
    }


    protected void turnLeft() {
        double angle = getAngle();
        angle -= turnSpeed;
        setAngle(angle);
    }

    protected void turnRight() {
        double angle = getAngle();
        angle += turnSpeed;
        setAngle(angle);
    }



    public void move(GameState gamestate){
        if(up){
            moveForward();
        }
        else if(down){
            moveBackward();
        }
        else if(right){
            turnRight();
        }
        else if(left){
            turnLeft();
        }
    }

    public void switchUpOn(){
        up = true;
    }

    public void switchDownOn(){
        down = true;
    }

    public void switchLeftOn(){
        left = true;
    }

    public void switchRightOn(){
        right = true;
    }
    public void switchOff(){
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public boolean isNew(){
        return isNew;
    }

    public void makeOld(){
        isNew = false;
    }

    public double getXBound(){
          return getX() + Xsize;
    }

    public double getYBound(){
          return getY() + Ysize;
    }

    public void setXSize(double X){
        Xsize = X;
    }

    public void setYSize(double Y){
        Ysize = Y;
    }

    public void shoot(GameState gamestate){

    }

    public void resetPowerUpCounter(){
        powerUpCounter=700;
    }

    public double getPowerUpCounter(){
        return powerUpCounter;
    }

    public void decrementPowerUp(){
        powerUpCounter-=1;
    }

    public String getOriginTank(){
        return originTank;
    }

    public void setOriginTank(String originTank){
        this.originTank = originTank;
    }

}
